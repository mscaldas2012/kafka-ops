package kafka.ops

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.PartitionInfo

/**
 *
 *
 * @Created - 5/29/20
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
interface IKafkaProxy {
    fun listTopics(): Array<String>
    fun createTopic(topicName: String, numOfPartitions: Int = 10, replicationFactor: Short = 3)
    fun deleteTopic(topicName: String)
    fun getTopicInfo(topicName: String): MutableList<TopicInfo>
    fun <T> getTopicContent(topicName: String, offset: Long = 0): List<T>
    fun <T> getTopicContent(topicName: String, partitionNumber: Int = 0, offSet: Long = 0): List<T>
    //fun <T> getTopicContent(consumer: KafkaConsumer<String, T>, partitions: List<TopicPartition>, offset: Long): List<T>
}