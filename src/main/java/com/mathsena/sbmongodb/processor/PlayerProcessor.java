package com.mathsena.sbmongodb.processor;

import com.mathsena.sbmongodb.model.Player;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class PlayerProcessor implements ItemProcessor<Player, Player> {
    @Override
    public Player process(Player item) throws Exception {
        return item;
    }
}
