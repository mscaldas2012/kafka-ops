package kafka.ops

import com.fasterxml.jackson.databind.JsonNode

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.*
import kafka.ops.security.S2SAuth
import kafka.ops.security.ServiceNotAuthorizedException
import org.apache.kafka.clients.consumer.ConsumerRecord
import java.util.logging.Logger

/**
 *
 *
 * @Created - 3/20/20
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@Controller("/kafka")
class KafkaController(val kafkaProxy: KafkaProxyJson, val s2sauth: S2SAuth) {
    companion object {
        val logger = Logger.getLogger(KafkaController::class.toString())
    }

    @Get("/topics")
    fun getTopics(@Header("s2s-token") token: String): Array<String> {
        logger.info("AUDIT - Retrieving list of Topics")
        s2sauth.checkS2SCredentials(token)
        return kafkaProxy.listTopics()
    }
    @Post("/topics/{topicName}") //Default values not workinng for Controller
    fun createTopic(@Header("s2s-token") token: String, topicName:String, @QueryValue nbrOfPartitions: Int? = 10, @QueryValue replicationFactor: Short? = 3  ): HttpResponse<String> {
        logger.info("AUDIT - Creating new Topic $topicName")
        s2sauth.checkS2SCredentials(token)
        kafkaProxy.createTopic(topicName, nbrOfPartitions?: 10, replicationFactor?: 3)
        return HttpResponse.ok("topic ${topicName} created")
    }

    @Get("topics/{topicName}")
    fun <T> listTopicContent(@Header("s2s-token") token: String, topicName: String, @QueryValue offset: Long? ): MutableHttpResponse<List<TopicRecord>>? {
        logger.info("AUDIT - Getting contents of topic $topicName")
        s2sauth.checkS2SCredentials(token)
        //val content = kafkaProxy.getTopicContent<T>(topicName, offset?:0 )
        val content = kafkaProxy.getConsumerRecords<T>(topicName, offset?:0)
        return HttpResponse.ok(content)
    }

    @Get("topics/{topicName}/{partition}")
    fun <T> listTopicContentForPartition(@Header("s2s-token") token: String, topicName: String, partition: Int, @QueryValue offset: Long?): MutableHttpResponse<List<T>>? {
        logger.info("AUDIT - Getting contents of topic $topicName / partition $partition")
        s2sauth.checkS2SCredentials(token)
        val content = kafkaProxy.getTopicContent<T>(topicName, partition, offset ?: 0)
        return HttpResponse.ok(content)
    }

    @Get("topics/{topicName}/info")
    fun getTopicInfo(@Header("s2s-token") token: String, topicName: String): MutableList<TopicInfo> {
        logger.info("AUDIT - Getting information for topic $topicName")
        s2sauth.checkS2SCredentials(token)
        return kafkaProxy.getTopicInfo(topicName)
    }

    @Delete("topics/{topicName}")
    fun deleteTopic(@Header("s2s-token") token: String, topicName: String): HttpResponse<Any>  {
        logger.info("AUDIT - Deleting topic $topicName")
        s2sauth.checkS2SCredentials(token)
        kafkaProxy.deleteTopic(topicName)
        return HttpResponse.ok()
    }

    @Error(exception = ServiceNotAuthorizedException::class)
    protected fun handleAuthorizationErrors(request: HttpRequest<*>, e: ServiceNotAuthorizedException): HttpResponse<Any> {
        logger.severe("ServiceNotAuthorizedException thrown: " + e.message)
        return HttpResponse.unauthorized()
    }
}