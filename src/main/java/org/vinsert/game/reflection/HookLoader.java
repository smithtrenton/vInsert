package org.vinsert.game.reflection;

import flexjson.JSONDeserializer;
import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.vinsert.game.reflection.model.ClassDefinition;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * @author : const_
 */
public class HookLoader {

    private Map<String, ClassDefinition> definitions = newHashMap();
    private static final JSONDeserializer<List<ClassDefinition>> deserializer = new JSONDeserializer<>();
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_" +
            "9_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36";
    private static final String HOOKS_URL = "http://vinsert.org/metadata/hooks.json";

    public HookLoader() {
        preload();
    }

    private void preload() {
        try {
            Content content = Request.Get(HOOKS_URL)
                    .version(HttpVersion.HTTP_1_1)
                    .userAgent(USER_AGENT)
                    .useExpectContinue()
                    .execute()
                    .returnContent();
            String hooks = content.asString();
            List<ClassDefinition> set = deserializer.deserialize(hooks);
            for (ClassDefinition item : set) {
                definitions.put(item.originalName, item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Problem with hooks!?", e);
        }
    }
}

