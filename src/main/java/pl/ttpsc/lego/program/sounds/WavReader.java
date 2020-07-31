package pl.ttpsc.lego.program.sounds;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import pl.ttpsc.lego.exception.FileFormatException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class WavReader {

    private final static Logger logger = Logger.getLogger(WavReader.class);

    public byte[] getBytesFromWav(Path path) {
        try {
            InputStream inputStream = AudioSystem.getAudioInputStream(path.toFile());
            return IOUtils.toByteArray(inputStream);
        } catch (UnsupportedAudioFileException | IOException e) {
            logger.error("Error while converting wav file", e);
        }
        return new byte[]{};
    }

    public AudioFileFormat getAudioFileFormat(Path path) {
        try {
            return AudioSystem.getAudioFileFormat(path.toFile());
        } catch (UnsupportedAudioFileException | IOException e) {
            logger.error("Error while checking wav file format", e);
        }
        throw new FileFormatException();
    }

}
