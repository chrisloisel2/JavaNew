package com.example.patterns.structural.facade;

public class VideoConversionFacade {

    public byte[] convert(String fileName, String targetFormat) {
        Decoder decoder = new Decoder();
        Encoder encoder = new Encoder(targetFormat);
        byte[] raw = decoder.decode(fileName);
        return encoder.encode(raw);
    }

    private static class Decoder {
        byte[] decode(String file) {
            return ("raw:" + file).getBytes();
        }
    }

    private static class Encoder {
        private final String format;

        Encoder(String format) {
            this.format = format;
        }

        byte[] encode(byte[] input) {
            return (format + new String(input)).getBytes();
        }
    }
}
