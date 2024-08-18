package se.isai.microservices.core.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.isai.microservices.core.user.dto.Event;
import se.isai.microservices.core.user.dto.User;
import se.isai.microservices.core.user.exceptions.EventProcessingException;

import java.util.function.Consumer;

@Configuration
public class MessageProcessorConfig {
    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);

    private final UserService userService;

    @Autowired
    MessageProcessorConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public Consumer<Event<String, User>> messageProcessor() {
        return event -> {
            LOG.info("Process message created at {}...", event.getEventCreatedAt());

            switch (event.getEventType()) {

                case CREATE:
                    User user = event.getData();
                    userService.createUser(user);
                    break;

                case DELETE:
                    String userId = event.getKey();
                    userService.deleteUser(userId);
                    break;

                default:
                    String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE or DELETE event";
                    LOG.warn(errorMessage);
                    throw new EventProcessingException(errorMessage);
            }

            LOG.info("Message processing done!");
        };
    }
}
