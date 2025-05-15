package com.bot.tg.meme.search.engine;

import java.io.File;
import java.io.IOException;

public interface FileProvider {
    byte[] getFile(String message) throws IOException;
}
