package ru.yandex.practicum.kafka.serializer;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;


public class DeviceAddedEventAvroDeserializer implements Deserializer<DeviceAddedEventAvro> {
    private final DecoderFactory decoderFactory = DecoderFactory.get();
    private final DatumReader<DeviceAddedEventAvro> reader = new SpecificDatumReader<>(DeviceAddedEventAvro.getClassSchema());


    @Override
    public DeviceAddedEventAvro deserialize(String topic, byte[] data) {
        try {
            if (data != null) {
                BinaryDecoder decoder = decoderFactory.binaryDecoder(data, null);
                return this.reader.read(null, decoder);
            }
            return null;
        } catch (Exception e) {
            throw new SerializationException("Ошибка десереализации данных из топика [" + topic + "]", e);
        }
    }
}