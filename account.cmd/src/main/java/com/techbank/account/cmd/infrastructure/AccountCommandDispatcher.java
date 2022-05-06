package com.techbank.account.cmd.infrastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.techbank.cqrs.core.commands.BaseCommand;
import com.techbank.cqrs.core.commands.CommandHandlerMethod;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;

@Service
public class AccountCommandDispatcher implements CommandDispatcher {
    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler) {
        List<CommandHandlerMethod> handlerMethods = routes.computeIfAbsent(type, c -> new ArrayList<>());
        handlerMethods.add(handler);
    }

    @Override
    public void send(BaseCommand command) {
        List<CommandHandlerMethod> handlerMethods = routes.get(command.getClass());
        if (CollectionUtils.isEmpty(handlerMethods)){
            throw new RuntimeException("No command handler was registered!");
        }
        if (handlerMethods.size() > 1){
            throw new RuntimeException("Cannot send command to more than one handler!");
        }
        handlerMethods.get(0).handle(command);
    }
}
