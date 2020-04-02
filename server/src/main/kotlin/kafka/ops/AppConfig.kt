package kafka.ops

import io.micronaut.context.annotation.ConfigurationProperties
import javax.validation.constraints.NotBlank

/**
 *
 *
 * @Created - 4/2/20
 * @Author Marcelo Caldas mcq1@cdc.gov
 */


@ConfigurationProperties(value = "app")
class AppConfig {
    @NotBlank
    lateinit var brokers: String
    @NotBlank
    lateinit var keyDeserializer: String
    @NotBlank
    lateinit var valueDeserializer: String


//    kafka:
//      bootstrap:
//          servers: localhost:9092
//
//      application.id: ${micronaut.application.name}-${random.int}
//      #application.id: validator-streamer--1695807200
//      client.id: ${micronaut.application.name}
//      commit.interval.ms: 1000
//      auto.offset.reset: earliest
//
//      default:
//          deserialization:
//              exception:
//                  handler: org.apache.kafka.streams.errors.LogAndContinueExceptionHandler
//          key:
//              serde: org.apache.kafka.common.serialization.Serdes$StringSerde
//              deserializer: org.apache.kafka.common.serialization.StringDeserializer
//          value:
//              serde: org.apache.kafka.common.serialization.Serdes$StringSerde
//              deserializer: org.apache.kafka.common.serialization.StringDeserializer
}