package kafka.ops

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.*
import javax.inject.Inject

/**
 * @Created - 3/20/20
 * @Author Marcelo Caldas mcq1@cdc.gov
 */

internal class KafkaProxyTest {

    val kafkaProxy: KafkaProxy = KafkaProxy()


    @Test
    fun listTopics() {
        kafkaProxy.listTopics().forEach { println(it) }
    }

    @Test
    fun testCreateTopic() {
        kafkaProxy.createTopic("unit-test-${UUID.randomUUID()}")
    }

    @Test
    fun testListTopicContent() {
        val msgs = kafkaProxy.getTopicContent("test1", 0)
        msgs.forEach { println(it) }
    }

    @Test
    fun testDeleteTopic() {
        kafkaProxy.createTopic("unit-test-delete-me")
        this.listTopics()
        kafkaProxy.deleteTopic("unit-test-delete-me")
        this.listTopics()
    }

    @Test
    fun testDeleteTopicWihtMessages() {
        kafkaProxy.deleteTopic("test1")
    }
    @Test
    fun testDeleteInvalidTopic() {
        kafkaProxy.deleteTopic("this-topic-does-not-exist")
    }

    @Test
    fun testGetTopicInfo() {
        val info = kafkaProxy.getTopicInfo("test1")
        println("topicInfo.startOffset = ${info.startOffset}")
        println("info.endOffset = ${info.endOffset}")
        info.partitions?.forEach { println("p -> $it")}
    }
}