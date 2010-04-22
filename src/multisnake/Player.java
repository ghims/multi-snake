/*
 *  Copyright (C) 2010 Patrick Hulin
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package multisnake;

import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.io.IOException;

/**
 *
 * @author poodimoos
 */
public abstract class Player implements Tickable, Externalizable {
    private Snake snake;
    private Game game;

    private int score;
    private int kills;

    private String name;

    public Player() {
        name = "";
    }

    public Player(String name) {
        this.name = name;
    }

    public void beginGame(Game game) {
        this.game = game;
        snake = new Snake();
        snake.reset(game.randomValidLocation());
    }

    public String getName() {
        return name;
    }

    public void tick() {
        snake.tick();
    }

    public Snake getSnake() {
        return snake;
    }

    public int getScore() {
        return score;
    }

    public int getKills() {
        return kills;
    }

    // overridden by subclasses
    public boolean isReady() {
        return (snake != null);
    }

    protected void setDirection(Direction dir) {
        snake.setDirection(dir);
    }

    protected Game getGame() {
        return game;
    }

    // methods for game events, things that can happen to a player
    public void eat() {
        score += 10;
        snake.appendSegment();
        game.makeNewFood();
    }

    public void kill() {
        score -= 20;
        snake.reset(game.randomValidLocation());
    }

    public void giveKill() {
        kills += 1;
        score += 30;
    }

    // methods for serialization
    public void writeExternal(ObjectOutput out) {
        try {
            out.writeObject(snake);
            out.writeObject(name);
            out.writeObject(new Integer(score));
            out.writeObject(new Integer(kills));
            out.flush();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public void readExternal(ObjectInput in) {
        try {
            snake = (Snake)(in.readObject());
            name = (String)(in.readObject());
            Integer scoreI = (Integer)(in.readObject());
            score = scoreI.intValue();
            Integer killsI = (Integer)(in.readObject());
            kills = killsI.intValue();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
