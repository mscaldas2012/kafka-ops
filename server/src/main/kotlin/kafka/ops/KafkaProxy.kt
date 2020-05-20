package kafka.ops

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition
import java.time.Duration
import java.util.*
import java.util.logging.Logger
import javax.inject.Singleton


/**
 *
 *
 * @Created - 3/20/20
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@Singleton
class KafkaProxy(val appConfig: AppConfig) {
    val POLL_TIME_OUT = 5_000

    companion object {
        val log = Logger.getLogger(KafkaProxy::class.java.name)
    }
    private fun getProperties(): Properties {
        println(appConfig.brokers)
        println(appConfig.keyDeserializer)
        println(appConfig.valueDeserializer)
        val props = Properties()

        props.put("bootstrap.servers", appConfig.brokers)
//        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
//        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        props.put("key.deserializer", appConfig.keyDeserializer)
        props.put("value.deserializer", appConfig.valueDeserializer)
        props.put("group.id", UUID.randomUUID().toString())
        return props
    }

    private fun getConsumer(): KafkaConsumer<String, String> {

//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        val consumer = KafkaConsumer<String, String>(getProperties())
        return consumer
    }

    fun listTopics():Array<String> {
        return getConsumer().listTopics().keys.filter { !it.startsWith("__")}.toTypedArray()
    }

    fun createTopic(topicName: String, numOfPartitions: Int = 10, replicationFactor: Short = 3) {
        log.info("Creating topic $topicName with $numOfPartitions partitions and $replicationFactor replication factor!")
        val adminClient: AdminClient = AdminClient.create(getProperties())
        val newTopic = NewTopic(topicName, numOfPartitions, replicationFactor)

        adminClient.createTopics(listOf(newTopic))
        adminClient.close()
    }

    fun deleteTopic(topicName: String) {
        log.info("Deleting Topic $topicName")
        val adminClient: AdminClient = AdminClient.create(getProperties())
        adminClient.deleteTopics(listOf(topicName))
        adminClient.close()
    }

    fun getTopicInfo(topicName: String): MutableList<TopicInfo> {
        val consumer = getConsumer()
        val partitions = consumer.partitionsFor(topicName)
        val partitionNumbers = partitions.map { it.partition() }
       // val partition = TopicPartition(topicName, partitionNumbers.first())
        //consumer.subscribe(listOf(topicName))

        val result = mutableListOf<TopicInfo>()
        partitionNumbers.forEach {
            val partition = TopicPartition(topicName, it)
            consumer.assign(listOf(partition))
            val topicInfo = TopicInfo(it)
            consumer.seekToBeginning(listOf(partition))
            topicInfo.startOffset = consumer.position(partition)
            consumer.seekToEnd(listOf(partition))
            topicInfo.endOffset = consumer.position(partition)
            result.add(topicInfo)
        }
        return result
    }

    fun getTopicContent(topicName: String, partitionNumber: Int = 0, offSet: Long = 0): List<String> {
        val consumer = getConsumer()
        val partitions = consumer.partitionsFor(topicName)
        val partition = TopicPartition(topicName, partitionNumber)
        //consumer.subscribe(listOf(topicName))
        consumer.assign(listOf(partition))
        consumer.seek(partition, offSet)
        val records = mutableListOf<String>()
        println("Querying topic...")
        val startPolling = Date()
        var continuePolling = true;
        do {
           val newRecords = consumer.poll(Duration.ofSeconds(1))
            records.addAll(newRecords.map { it.value()} )
            val endPolling = Date()
            continuePolling= (newRecords.count() == 0) && (endPolling.time - startPolling.time < POLL_TIME_OUT)
            println("found ${newRecords.count()} records")
        } while (continuePolling)
        return records
    }

}
 class TopicInfo(val partitionNumber: Int) {
    var startOffset: Long = 0
    var endOffset: Long = 0
 }