package kafka.training.day4.avrosender;

import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import kafka.training.day4.avroschema.CustomerSchema;

/**
 * 
 * @author kafka
 *
 */

public class CustomerSender {

	public static void main(String... args) throws InterruptedException {

		String schemaURL = "http://localhost:8081";

		Properties properties = new Properties();
		properties.setProperty("schema.registry.url", schemaURL);
		properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.setProperty("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
		properties.setProperty("bootstrap.servers", "localhost:9092");

		KafkaProducer<String, GenericRecord> producer = new KafkaProducer<String, GenericRecord>(properties);

		Schema.Parser parser = new Schema.Parser();
		Schema schema = parser.parse(CustomerSchema.SCHEMA_DEFINITION);

		for (int i = 1; i <= 50; i++) {
			GenericRecord customer = new GenericData.Record(schema);
			customer.put("id", 10000+i);
			customer.put("name", "Naveen-"+(10000+i));

			ProducerRecord<String, GenericRecord> record = new ProducerRecord<>("customerTopic", "customer1", customer);
			producer.send(record);
			Thread.sleep(500);
		}
		producer.close();

		/**
		 * Before running this code, run the following code from confluence\bin
		 * directory inorder to recieve this sudo ./kafka-avro-console-consumer --topic
		 * ordersTopic --bootstrap-server localhost:9092 --from-beginning
		 */

	}

}
