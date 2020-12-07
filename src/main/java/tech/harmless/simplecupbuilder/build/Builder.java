package tech.harmless.simplecupbuilder.build;

//TODO Async instead of Thread?
//TODO Move build stuff from BuildManager to this class!
public class Builder implements Runnable {

    public Builder() {

    }

    public void triggerBuild() {

    }

    @Override
    public void run() {
        throw new RuntimeException("Not implemented!");
    }
}
