package com.bot.tg.meme.service;


import com.bot.tg.meme.integrations.NlpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EmbeddingServiceAI implements EmbeddingService {

//    private static String PROMT_PATTERN = """
//    Используй слова из мемов, чтобы кратко описать на английском следующий дилог, должно получиться меньше 5 слов.
//    В ответе не должно содержаться ничего кроме краткого текста.
//    Текст: "%s"
//    """;
    private static String PROMT_PATTERN = """
    Translate the following Russian conversation to English, then rewrite it using meme-style slang or internet humor. Your output should be a short, funny reaction (less than 5 words) that could be used to search for a GIF on Giphy. Return only the meme-style phrase, no explanations.
    
    Text: "%s"
    """;

    @Autowired
    NlpClient client;

    @Override
    public Optional<String> getEmbeddings(List<String> text) {
        return client.getResponse(
            PROMT_PATTERN.formatted(
                text.stream()
                    .map(String::strip)
                    .map(it -> "-" + it)
                    .map(StringBuilder::new)
                    .map(it -> it.append(".\n"))
                    .reduce(new StringBuilder(), StringBuilder::append).toString()
            )
        ).map(it -> it.split("\\n")[0]);
    }
}
