package pl.ttpsc.lego.communication.service;

import lejos.hardware.Audio;
import org.apache.log4j.Logger;
import pl.ttpsc.lego.communication.LegoBrickDevice;
import pl.ttpsc.lego.program.sounds.WavReader;

import javax.sound.sampled.AudioFileFormat;
import java.nio.file.Path;

public class LegoBrick {
    
    private final static Logger logger = Logger.getLogger(LegoBrick.class);

    private final LegoBrickDevice legoBrickDevice;

    private final static WavReader wavReader = new WavReader();
    
    public LegoBrick(final LegoBrickDevice legoBrickDevice) {
        this.legoBrickDevice = legoBrickDevice;
    }

    public String getName() {
        return legoBrickDevice.getRemoteEV3().getName();
    }
    
    public Audio getAudio() {
        return legoBrickDevice.getRemoteEV3().getAudio();
    }

    public float getBatteryCurrent() {
        return legoBrickDevice.getRemoteEV3().getPower().getBatteryCurrent();
    }

//    public float getMotorCurrent() {
//        return legoBrickDevice.getRemoteEV3().getPower().getMotorCurrent();
//    }

    public float getVoltage() {
        return legoBrickDevice.getRemoteEV3().getPower().getVoltage();
    }

//    public float getPowerConsumption() {
//        return legoBrickDevice.getRemoteEV3().getPower().getVoltage();
//    }

    public void playSound(final Path filePath) {
        if (filePath.toFile().exists()) {
            byte[] soundSamples = wavReader.getBytesFromWav(filePath);
            AudioFileFormat fileFormat = wavReader.getAudioFileFormat(filePath);
            int numberOfSamples = soundSamples.length / fileFormat.getFormat()
                                                                  .getSampleSizeInBits();
            legoBrickDevice.getRemoteEV3().getAudio().playSample(soundSamples, 0, soundSamples.length, 8000, 100);
        } else {
            logger.error(
                    "Sound you would like to play on EV3 does not exists in location: " + filePath
                            .toString());
        }
    }
}
