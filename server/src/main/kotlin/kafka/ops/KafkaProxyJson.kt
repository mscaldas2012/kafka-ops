package kafka.ops

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
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
class KafkaProxyJson(val appConfig: AppConfig) : IKafkaProxy {
    val POLL_TIME_OUT = 5_000

    companion object {
        val log = Logger.getLogger(KafkaProxyJson::class.java.name)
    }
    private fun getProperties(): Properties {
        println(appConfig.brokers)
        println(appConfig.keyDeserializer)
        println(appConfig.valueDeserializer)
        val props = Properties()

        props.put("bootstrap.servers", appConfig.brokers)
        props.put("key.deserializer", appConfig.keyDeserializer)
        props.put("value.deserializer", appConfig.valueDeserializer)
        props.put("group.id", UUID.randomUUID().toString())
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true)
        return props
    }

    private fun <T> getConsumer(): KafkaConsumer<String, T> {
        val consumer = KafkaConsumer<String, T>(getProperties())
        return consumer
    }

    override fun listTopics():Array<String> {
        return getConsumer<String>().listTopics().keys.filter { !it.startsWith("__")}.toTypedArray()
    }

    override fun createTopic(topicName: String, numOfPartitions: Int, replicationFactor: Short) {
        log.info("Creating topic $topicName with $numOfPartitions partitions and $replicationFactor replication factor!")
        val adminClient: AdminClient = AdminClient.create(getProperties())
        val newTopic = NewTopic(topicName, numOfPartitions, replicationFactor)

        adminClient.createTopics(listOf(newTopic))
        adminClient.close()
    }

    override fun deleteTopic(topicName: String) {
        log.info("Deleting Topic $topicName")
        val adminClient: AdminClient = AdminClient.create(getProperties())
        adminClient.deleteTopics(listOf(topicName))
        adminClient.close()
    }

    override fun getTopicInfo(topicName: String): MutableList<TopicInfo> {
        val consumer = getConsumer<String>()
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



    override fun <T> getTopicContent(topicName: String, partitionNumber: Int, offSet: Long): List<T> {
        val consumer = getConsumer<T>()
        //val partitions = consumer.partitionsFor(topicName)
        val partition = TopicPartition(topicName, partitionNumber)
        return getTopicContent(consumer, listOf(partition), offSet)
//        //consumer.subscribe(listOf(topicName))
//        consumer.assign(listOf(partition))
//        consumer.seek(partition, offSet)
//        val records = mutableListOf<T>()
//        println("Querying topic...")
//        val startPolling = Date()
//        var continuePolling = true;
//        do {
//           val newRecords = consumer.poll(Duration.ofSeconds(1))
//            records.addAll(newRecords.map { it.value()} )
//            val endPolling = Date()
//            continuePolling= (newRecords.count() == 0) && (endPolling.time - startPolling.time < POLL_TIME_OUT)
//            println("found ${newRecords.count()} records")
//        } while (continuePolling)
//        return records
    }
    override fun <T> getTopicContent(topicName: String, offset: Long): List<T> {
        val consumer = getConsumer<T>()
        val partitions = consumer.partitionsFor(topicName)
        val topicPartitions = partitions.map { TopicPartition(topicName, it.partition())}
        return getTopicContent(consumer, topicPartitions, offset)
    }

     private fun <T> getTopicContent(consumer: KafkaConsumer<String, T>, partitions: List<TopicPartition>, offset: Long): List<T> {
        consumer.assign(partitions)
         partitions.forEach {
             consumer.seek(it, offset)
         }
        val records = mutableListOf<T>()
        println("Querying topic...")
        val startPolling = Date()
        var continuePolling = true;
        do {
            val newRecords = consumer.poll(Duration.ofSeconds(1))
            records.addAll(newRecords.map {it.value()} )
            val endPolling = Date()
            continuePolling= (newRecords.count() == 0) && (endPolling.time - startPolling.time < POLL_TIME_OUT)
            println("found ${newRecords.count()} records")
        } while (continuePolling)
        return records
    }

    fun <T> getConsumerRecords(topicName: String, offset: Long): List<TopicRecord> {
        val consumer = getConsumer<T>()
        val partitions = consumer.partitionsFor(topicName)
        val topicPartitions = partitions.map { TopicPartition(topicName, it.partition())}
        consumer.assign(topicPartitions)
        topicPartitions.forEach {
            consumer.seek(it, offset)
        }
        val records = mutableListOf<TopicRecord>()
        println("Querying topic...")
        val startPolling = Date()
        var continuePolling = true;
        do {
            val newRecords = consumer.poll(Duration.ofSeconds(1))
            records.addAll(newRecords.map {TopicRecord(it.offset(), it.partition(), it.key(), it.value())} )
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