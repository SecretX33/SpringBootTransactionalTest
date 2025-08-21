package com.secretx33.springboottransactionaltest.controller

import com.secretx33.springboottransactionaltest.model.Person
import com.secretx33.springboottransactionaltest.service.SuspendService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/suspend")
class SuspendController(
    private val suspendService: SuspendService,
) {
    /**
     * Runs entirely on the same tomcat thread.
     */
    @PostMapping("/1")
    fun normalTransactional(): Person = suspendService.normalTransactional()

    /**
     * Runs entirely on the same tomcat thread.
     */
    @PostMapping("/2")
    fun normalTransactionalWithRunBlocking(): Person = runBlocking {
        suspendService.normalTransactional()
    }

    /**
     * Starts on a tomcat thread, but resumes on a different thread after suspending.
     *
     * ```
     * - [suspendTransactional] current thread before suspending: http-nio-8500-exec-2 ([Context0{}, MonoCoroutine{Active}@2f267989, Dispatchers.Unconfined])
     * - [suspendTransactional] current thread after resuming: kotlinx.coroutines.DefaultExecutor ([Context0{}, MonoCoroutine{Active}@2f267989, Dispatchers.Unconfined])
     * ```
     */
    @PostMapping("/3")
    suspend fun suspendTransactional(): Person = suspendService.suspendTransactional()

    /**
     * Runs entirely on the same tomcat thread.
     *
     * ```
     * - [suspendTransactional] current thread before suspending: http-nio-8500-exec-8 ([Context0{}, MonoCoroutine{Active}@41b88e5f, BlockingEventLoop@5ef6503e])
     * - [suspendTransactional] current thread after resuming: http-nio-8500-exec-8 ([Context0{}, MonoCoroutine{Active}@41b88e5f, BlockingEventLoop@5ef6503e])
     * ```
     */
    @PostMapping("/4")
    fun suspendTransactionalWithRunBlocking(): Person = runBlocking {
        suspendService.suspendTransactional()
    }

    /**
     * Starts on a tomcat thread, but resumes on a different thread after suspending.
     *
     * ```
     * - [transactionalFlow] current thread before suspending: http-nio-8500-exec-2 ([FlowSubscription{Active}@577090ce, Dispatchers.Unconfined])
     * - [transactionalFlow] current thread after resuming: kotlinx.coroutines.DefaultExecutor ([FlowSubscription{Active}@577090ce, Dispatchers.Unconfined])
     * ```
     */
    @PostMapping("/5")
    fun transactionalFlow(): Flow<Person> = suspendService.transactionalFlow()

    /**
     * Starts on a tomcat thread, but resumes on a different thread after suspending.
     *
     * ```
     * - [suspendTransactionalFlow] current thread before suspending: http-nio-8500-exec-6 ([FlowSubscription{Active}@339fd6df, Dispatchers.Unconfined])
     * - [suspendTransactionalFlow] current thread after resuming: kotlinx.coroutines.DefaultExecutor ([FlowSubscription{Active}@339fd6df, Dispatchers.Unconfined])
     * ```
     */
    @PostMapping("/6")
    suspend fun suspendTransactionalFlow(): Flow<Person> = suspendService.suspendTransactionalFlow()

    /**
     * Starts already on a thread from the DefaultExecutor.
     *
     * ```
     * [suspendTransactionalFlow] current thread before suspending: kotlinx.coroutines.DefaultExecutor ([FlowSubscription{Active}@5f15c8bd, Dispatchers.Unconfined])
     * [suspendTransactionalFlow] current thread after resuming: kotlinx.coroutines.DefaultExecutor ([FlowSubscription{Active}@5f15c8bd, Dispatchers.Unconfined])
     * ```
     */
    @PostMapping("/7")
    fun suspendTransactionalFlowWithRunBlocking(): Flow<Person> = runBlocking {
        suspendService.suspendTransactionalFlow()
    }

    /**
     * Starts on a tomcat thread, but resumes on a different thread after suspending.
     *
     * ```
     * - [transactionalFlow] current thread before suspending: http-nio-8500-exec-2 ([FlowSubscription{Active}@c37f08d, Dispatchers.Unconfined])
     * - [transactionalFlow] current thread after resuming: kotlinx.coroutines.DefaultExecutor ([FlowSubscription{Active}@c37f08d, Dispatchers.Unconfined])
     * ```
     */
    @PostMapping("/8")
    fun flowWithTransactionalFlow(): Flow<Person> = flow {
        emitAll(suspendService.transactionalFlow())
    }

    /**
     * Runs entirely on the same tomcat thread.
     *
     * ```
     * - [transactionalFlow] current thread before suspending: http-nio-8500-exec-8 ([BlockingCoroutine{Active}@7e6d6c0e, BlockingEventLoop@22186c47])
     * - [transactionalFlow] current thread after resuming: http-nio-8500-exec-8 ([BlockingCoroutine{Active}@7e6d6c0e, BlockingEventLoop@22186c47])
     *```
     *
     * But then it throws an exception because the flow is not thread-safe and concurrent emissions are prohibited:
     * ```
     * - java.lang.IllegalStateException: Flow invariant is violated:
     * 		Emission from another coroutine is detected.
     * 		Child of BlockingCoroutine{Active}@7e6d6c0e, expected child of FlowSubscription{Active}@5eac1702.
     * 		FlowCollector is not thread-safe and concurrent emissions are prohibited.
     * 		To mitigate this restriction please use 'channelFlow' builder instead of 'flow'
     * ```
     */
    @PostMapping("/9")
    fun flowWithTransactionalFlowWithRunBlocking(): Flow<Person> = flow {
        runBlocking {
            emitAll(suspendService.transactionalFlow())
        }
    }

    /**
     * Starts on a tomcat thread, but resumes on a different thread after suspending.
     *
     * ```
     * - [transactionalFlow] current thread before suspending: http-nio-8500-exec-4 ([ProducerCoroutine{Active}@2ab78412, Dispatchers.Unconfined])
     * - [transactionalFlow] current thread after resuming: kotlinx.coroutines.DefaultExecutor ([ProducerCoroutine{Active}@2ab78412, Dispatchers.Unconfined])
     * ```
     */
    @PostMapping("/10")
    fun channelFlowWithTransactionalFlow(): Flow<Person> = channelFlow {
        val result = runCatching {
            suspendService.transactionalFlow().collect(::send)
        }.exceptionOrNull()
        close(result)
        awaitClose()
    }

    /**
     * Runs entirely on the same tomcat thread.
     *
     * ```
     * - [transactionalFlow] current thread before suspending: http-nio-8500-exec-8 ([BlockingCoroutine{Active}@603e50d7, BlockingEventLoop@d642a68])
     * - [transactionalFlow] current thread after resuming: http-nio-8500-exec-8 ([BlockingCoroutine{Active}@603e50d7, BlockingEventLoop@d642a68])
     * ```
     */
    @PostMapping("/11")
    fun channelFlowWithTransactionalFlowWithRunBlocking(): Flow<Person> = channelFlow {
        val result = runCatching {
            runBlocking {
                suspendService.transactionalFlow().collect(::send)
            }
        }.exceptionOrNull()
        close(result)
        awaitClose()
    }

    /**
     * Runs entirely on the same tomcat thread.
     */
    @PostMapping("/12")
    fun flowWithNormalTransactional(): Flow<Person> = flow {
        emit(suspendService.normalTransactional())
    }

    /**
     * Starts on a tomcat thread, but resumes on a different thread after suspending.
     *
     * ```
     * - [suspendTransactional] current thread before suspending: http-nio-8500-exec-6 ([Context0{}, MonoCoroutine{Active}@294537ae, Dispatchers.Unconfined])
     * - [suspendTransactional] current thread after resuming: kotlinx.coroutines.DefaultExecutor ([Context0{}, MonoCoroutine{Active}@294537ae, Dispatchers.Unconfined])
     * ```
     */
    @PostMapping("/13")
    fun flowWithSuspendTransactional(): Flow<Person> = flow {
        emit(suspendService.suspendTransactional())
    }

    /**
     * Runs entirely on the same tomcat thread.
     *
     * ```
     * - [suspendTransactional] current thread before suspending: http-nio-8500-exec-1 ([Context0{}, MonoCoroutine{Active}@d280627, BlockingEventLoop@7a9a98a7])
     * - [suspendTransactional] current thread after resuming: http-nio-8500-exec-1 ([Context0{}, MonoCoroutine{Active}@d280627, BlockingEventLoop@7a9a98a7])
     * ```
     */
    @PostMapping("/14")
    fun flowWithSuspendTransactionalWithRunBlocking(): Flow<Person> = flow {
        emit(runBlocking {
            suspendService.suspendTransactional()
        })
    }
}