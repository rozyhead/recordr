package jp.co.sunarch.apps.recordr

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class RecordrApplication

fun main(args: Array<String>) {
    SpringApplication.run(RecordrApplication::class.java, *args)
}
