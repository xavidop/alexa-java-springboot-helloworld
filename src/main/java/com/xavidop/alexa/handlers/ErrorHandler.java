package com.xavidop.alexa.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.xavidop.alexa.localization.LocalizationManager;

import java.util.Optional;

public class ErrorHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        final String speechOutput =  LocalizationManager.getInstance().getMessage("ERROR_MSG");
        return handlerInput.getResponseBuilder()
                .withSpeech(speechOutput)
                .withReprompt(speechOutput)
                .build();
    }
}