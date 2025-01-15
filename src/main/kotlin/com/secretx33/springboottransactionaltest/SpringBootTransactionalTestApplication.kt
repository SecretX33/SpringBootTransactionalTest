package com.secretx33.springboottransactionaltest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBootTransactionalTestApplication

fun main(args: Array<String>) {
	runApplication<SpringBootTransactionalTestApplication>(*args)
}
