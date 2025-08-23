package com.keko.affix.stylePointsManager.soundSystem;

import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class Sound {

    private int bufferID;
    private int sourceID;
    private String filePath;
    private double volume = 0.5f;
    private ShortBuffer rawAudioBuffer;
    private int sampleRate;
    private int channels;
    private boolean isPlaying = false;
    private static final int AL_SEC_OFFSET = 0x1024;

    public Sound(String filePath, boolean loops) {
        this.filePath = filePath;

        MemoryStack.stackPush();
        IntBuffer channelsBuffer = MemoryStack.stackMallocInt(1);
        MemoryStack.stackPush();
        IntBuffer sampleRateBuffer = MemoryStack.stackMallocInt(1);

        rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(filePath, channelsBuffer, sampleRateBuffer);
        if (rawAudioBuffer == null) {
            System.out.println("could not load sound " + filePath);
            MemoryStack.stackPop();
            MemoryStack.stackPop();
            return;
        }


        channels = channelsBuffer.get();
        sampleRate = sampleRateBuffer.get();

        MemoryStack.stackPop();
        MemoryStack.stackPop();

        int format = -1;
        if (channels == 1)
            format = AL10.AL_FORMAT_MONO16;
        else if (channels == 2)
            format = AL10.AL_FORMAT_STEREO16;

        if (format == -1) {
            System.err.println("Unsupported channel count: " + channels + " for file: " + filePath);
            AL10.alDeleteBuffers(bufferID);
            if (rawAudioBuffer != null) {
                rawAudioBuffer.clear();
            }
            return;
        }

        bufferID = AL10.alGenBuffers();
        AL10.alBufferData(bufferID, format, rawAudioBuffer, sampleRate);

        sourceID = AL10.alGenSources();


        AL10.alSourcei(sourceID, AL10.AL_BUFFER, bufferID); // idk
        AL10.alSourcei(sourceID, AL10.AL_LOOPING, loops ? 1 : 0); //loops
        AL10.alSourcei(sourceID, AL10.AL_POSITION, 0); //pos
        AL10.alSourcef(sourceID, AL10.AL_GAIN, (float) volume); //volume
        AL10.alSourcei(sourceID, AL10.AL_SOURCE_RELATIVE, AL10.AL_TRUE);
        AL10.alSource3f(sourceID, AL10.AL_POSITION, 0.0f, 0.0f, 0.0f);
        AL10.alSource3f(sourceID, AL10.AL_VELOCITY, 0.0f, 0.0f, 0.0f);
    }

    public void setVolume(double volume) {
        this.volume = volume;
        AL10.alSourcef(sourceID, AL10.AL_GAIN, (float) volume); //volume
    }

    public void setPosition(float x, float y, float z){
        AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, z);
    }


    public float getAmplitude() {
        try {
            float seconds = AL10.alGetSourcef(sourceID, AL_SEC_OFFSET);
            int sampleIndex = (int) (seconds * sampleRate) * channels;
            if (rawAudioBuffer == null || sampleIndex >= rawAudioBuffer.limit())
                return 0;

            int chunkSize = 1024 * 8;

            float sum = 0;

            for (int i = sampleIndex; i < sampleIndex + chunkSize; i += 1) {
                short sample = rawAudioBuffer.get(i);
                sum += (float) Math.abs(sample) / 32768;
            }

            return sum / chunkSize;
        }catch (Exception e){
            this.stop();
            return 0;
        }
    }

    public double getVolume() {
        return volume;
    }

    public void delete(){
        AL10.alDeleteSources(sourceID);
        AL10.alDeleteBuffers(bufferID);
    }

    public void play(){
        int state = AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE);
        if (state == AL10.AL_STOPPED) {
            isPlaying = false;
            AL10.alSourcei(sourceID, AL10.AL_POSITION, 0);
        }


        if (!isPlaying){
            AL10.alSourcePlay(sourceID);
            isPlaying = true;
        }
    }

    public void stop(){
        if (isPlaying){
            AL10.alSourceStop(sourceID);
            isPlaying = false;
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public String getName(){
        String name = new File(filePath).getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return name;
        }
        return name.substring(0, lastIndexOf);
    }

    public boolean isPlaying() {
        int state = AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE);
        if (state == AL10.AL_STOPPED)
            isPlaying = false;

        return isPlaying;
    }

}
