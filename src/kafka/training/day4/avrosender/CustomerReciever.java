package kafka.training.day4.avrosender;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.fasterxml.jackson.databind.ObjectMapper;

import kafka.training.day4.domain.Customer;

public class CustomerReciever {

	public static void main(String... args) {

		String schemaURL = "http://localhost:8081";

		Properties properties = new Properties();
		properties.setProperty("schema.registry.url", schemaURL);
		properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.setProperty("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer ");
		properties.setProperty("bootstrap.servers", "localhost:9092");
		properties.setProperty("group.id", "customer-group");

		KafkaConsumer<String, GenericRecord> consumer = new KafkaConsumer<>(properties);
		consumer.subscribe(Collections.singletonList("customerTopic"));

		while (true) {
			ConsumerRecords<String, GenericRecord> records = consumer.poll(Duration.ofSeconds(20));

			records.forEach(record -> {
				System.out.println(record.key());
				ObjectMapper mapper = new ObjectMapper();
				Customer customer;
				try {
					customer = mapper.readValue(record.value().toString().getBytes(), Customer.class);
					System.out.println(customer.getClass() + " | " + customer.getName());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			});
		}

	}

}
