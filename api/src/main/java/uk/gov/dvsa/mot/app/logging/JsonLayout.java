package uk.gov.dvsa.mot.app.logging;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.ThreadContext.ContextStack;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.message.Message;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/**
 * JSONLayout generate custom JSON layout
 * It serializes as a basic fields: thread, timestamp, logLevel, message and class name from with log became
 * MDC as a contextMap
 * NDC as a ndc field if set
 */
@Plugin(name = "JSONLayout", category = "Core", elementType = "layout", printObject = true)
public class JsonLayout extends AbstractStringLayout {
    public static final Charset UTF8 = Charset.forName("UTF-8");

    private final JsonFactory jsonFactory = new JsonFactory();

    /**
     * @param charset
     */
    protected JsonLayout(final Charset charset) {
        super(charset);
    }

    @Override
    public String toSerializable(final LogEvent event) {
        try {
            final StringWriter stringWriter = new StringWriter();
            final JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(stringWriter);
            jsonGenerator.writeStartObject();
            writeBasicFields(event, jsonGenerator);
            writeMessageField(event, jsonGenerator);
            writeMdc(event, jsonGenerator);
            writeNdc(event, jsonGenerator);
            writeThrowableEvents(event, jsonGenerator);
            jsonGenerator.writeEndObject();
            jsonGenerator.close();
            stringWriter.append("\n");
            return stringWriter.toString();
        } catch (IOException e) {
            LOGGER.error("Could not write event as JSON", e);
        }
        return StringUtils.EMPTY;
    }


    private static void writeBasicFields(final LogEvent event, final JsonGenerator jsonGenerator)
            throws IOException {
        Timestamp timestamp = new Timestamp((event.getTimeMillis()));

        jsonGenerator.writeStringField("thread", event.getThreadName());
        jsonGenerator.writeStringField("timestamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(timestamp));
        jsonGenerator.writeStringField("level", event.getLevel().toString());
        jsonGenerator.writeStringField("loggerName", event.getLoggerName());
    }

    private static void writeMessageField(final LogEvent event,
                                          final JsonGenerator jsonGenerator) throws IOException {

        final Message message = event.getMessage();
        if (message == null) {
            return;
        }

        if (message instanceof MapMessage) {
            final MapMessage mapMessage = (MapMessage) message;
            final Map<String, String> map = mapMessage.getData();
            writeStringMap("message", map, jsonGenerator);
        } else {
            jsonGenerator.writeStringField("message", message.getFormattedMessage());
        }
    }

    private static void writeMdc(final LogEvent event, final JsonGenerator jsonGenerator) throws IOException {
        writeStringMap("contextMap", event.getContextData().toMap(), jsonGenerator);
    }

    private static void writeNdc(final LogEvent event, final JsonGenerator jsonGenerator) throws IOException {
        final ContextStack ndc = event.getContextStack();
        if (ndc == null || ndc.getDepth() == 0) {
            return;
        }

        final List<String> ndcList = ndc.asList();
        jsonGenerator.writeArrayFieldStart("ndc");
        for (final String stackElement : ndcList) {
            jsonGenerator.writeString(stackElement);
        }
        jsonGenerator.writeEndArray();
    }

    /**
     * @param mapName
     * @param stringMap
     * @param jsonGenerator
     * @throws IOException
     */
    private static void writeStringMap(final String mapName, final Map<String, String> stringMap, final JsonGenerator jsonGenerator)
            throws IOException {
        if (stringMap == null || stringMap.isEmpty()) {
            return;
        }

        jsonGenerator.writeFieldName(mapName);
        jsonGenerator.writeStartObject();

        // TreeSet orders fields alphabetically by key:
        final Set<String> keys = new TreeSet<String>(stringMap.keySet());
        for (final String key : keys) {
            if (key.equals(LoggerParamsManager.URL_PARAMS_KEY)) {
                jsonGenerator.writeFieldName(LoggerParamsManager.URL_PARAMS_KEY);
                jsonGenerator.writeRawValue(stringMap.get(key));
            } else {
                jsonGenerator.writeStringField(key, stringMap.get(key));
            }
        }

        jsonGenerator.writeEndObject();
    }

    private static void writeThrowableEvents(final LogEvent event,
                                             final JsonGenerator jsonGenerator) throws IOException {
        final Throwable thrown = event.getThrown();
        if (thrown == null) {
            return;
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        thrown.printStackTrace(pw);

        String throwableString = sw.toString();
        if (throwableString.isEmpty()) {
            return;
        }

        jsonGenerator.writeStringField("throwable", throwableString);
    }

    @PluginFactory
    public static JsonLayout createLayout(
            @PluginAttribute("charset") final String charsetName) {
        Charset charset = UTF8;
        if (charsetName != null) {
            if (Charset.isSupported(charsetName)) {
                charset = Charset.forName(charsetName);
            } else {
                LOGGER.error(String.format("Charset %s is not supported for layout, using %s", charsetName, charset.displayName()));
            }
        }
        return new JsonLayout(charset);
    }
}
