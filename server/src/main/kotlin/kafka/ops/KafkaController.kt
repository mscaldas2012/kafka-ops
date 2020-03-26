package kafka.ops

import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import java.util.*
import java.util.logging.Logger

/**
 *
 *
 * @Created - 3/20/20
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@Controller("/kafka")
class KafkaController(val kafkaProxy: KafkaProxy) {
    companion object {
        val logger = Logger.getLogger(KafkaController::class.toString())
    }

    @Get("/topics")
    fun getTopics(): Array<String> {
        logger.info("AUDIT - Retrieving list of Topics")
        return kafkaProxy.listTopics()
    }
    @Post("/topics/{topicName}")
    fun createTopic(topicName:String ): HttpResponse<String> {
        logger.info("AUDIT - Creating new Topic $topicName")
        kafkaProxy.createTopic(topicName)
        return HttpResponse.ok("topic ${topicName} created")
    }

    @Get("topics/{topicName}")
    fun listTopicContent(topicName: String): MutableHttpResponse<List<String>>? {
        logger.info("AUDIT - Getting contents of topic $topicName")
        val content = kafkaProxy.getTopicContent(topicName)
        return HttpResponse.ok(content)
    }

    @Get("topics/{topicName}/info")
    fun getTopicInfo(topicName: String): TopicInfo {
        logger.info("AUDIT - Getting information for topic $topicName")
        return kafkaProxy.getTopicInfo(topicName)
    }

    @Delete("topics/{topicName}")
    fun deleteTopic(topicName: String): HttpResponse<Any>  {
        logger.info("AUDIT - Deleting topic $topicName")
        kafkaProxy.deleteTopic(topicName)
        return HttpResponse.ok()
    }
}