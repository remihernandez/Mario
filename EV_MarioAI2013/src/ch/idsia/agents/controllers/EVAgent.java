package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

/**
 * Created with IntelliJ IDEA.
 * User: Thibaut Royer
 * Date: 24/08/13
 * Time: 19:25
 */

public class EVAgent extends BasicMarioAIAgent implements Agent
{
    int trueJumpCounter = 0;
    int trueSpeedCounter = 0;
    int jumpCounter = 0;
    // Never ever modify these lines !
    public EVAgent()
    {
        super("EVAgent");
        reset();
    }

    // Called at level start
    public void reset()
    {
        action = new boolean[Environment.numberOfKeys];
        action[Mario.KEY_RIGHT] = true;
        //action[Mario.KEY_SPEED] = true;
        trueJumpCounter = 0;
        trueSpeedCounter = 0;
    }

    private boolean DangerOfAny(int pos_front, int pos_front_up, int pos_front_down, int pos_front_down_2, int pos_down)
    {

        if (pos_front_down_2 >= 80 || (pos_front_up != 0 && pos_front_up != 2) || pos_front_down == 0 && pos_down == -60)
        /*(getReceptiveFieldCellValue(marioEgoRow + 2, marioEgoCol + 1) == 0 &&
                getReceptiveFieldCellValue(marioEgoRow + 1, marioEgoCol + 1) == 0) ||
                getReceptiveFieldCellValue(marioEgoRow, marioEgoCol + 1) != 0 ||
                getReceptiveFieldCellValue(marioEgoRow, marioEgoCol + 2) != 0 ||
                getEnemiesCellValue(marioEgoRow, marioEgoCol + 1) != 0 ||
                getEnemiesCellValue(marioEgoRow, marioEgoCol + 2) != 0)*/
            return true;
        else
            return false;
    }

    public boolean[] getAction()
    {  // this Agent requires observation integrated in advance.
        int     pos_front = getReceptiveFieldCellValue(9,10);
        int     pos_front_up = getReceptiveFieldCellValue(8, 10);
        int     pos_front_down = getReceptiveFieldCellValue(10, 10);
        int     pos_front_down_2 = getReceptiveFieldCellValue(11, 10);
        int     pos_down = getReceptiveFieldCellValue(10, 9);
        int     pos_front3_down = getReceptiveFieldCellValue(10,12);
        int     pos_up = getReceptiveFieldCellValue(8, 9);
        int     mob_front = mergedObservation[8][10];
        int     mob_front_down = mergedObservation[9][10];
        int     mob_2front = mergedObservation[8][11];
        int     mob_2front_down = mergedObservation[9][11];
        int     mob_2front_2down = mergedObservation[10][12];
        int     mob_3front_2down = mergedObservation[10][13];

        /*Stop Little jump*/
        if (jumpCounter == 1)
        {
            jumpCounter = 0;

            if (action[Mario.KEY_JUMP] == true)
                action[Mario.KEY_JUMP] = false;
            else
                action[Mario.KEY_JUMP] = true;
            return (action);
        }

        /*Accelere avant un trou*/
        if (pos_front3_down == 0 && pos_down == -60)
            action[Mario.KEY_SPEED] = true;

        /*Big Jump*/
        if (DangerOfAny(pos_front, pos_front_up, pos_front_down, pos_front_down_2, pos_down) && getReceptiveFieldCellValue(marioEgoRow, marioEgoCol + 1) != 1)  // a coin  da
        {
            if (isMarioAbleToJump || (!isMarioOnGround && action[Mario.KEY_JUMP]))
            {
                action[Mario.KEY_JUMP] = true;
            }
            action[Mario.KEY_SPEED] = true;
            trueJumpCounter++;
        }

        else if (trueJumpCounter != 0)
        {
            action[Mario.KEY_JUMP] = false;
            action[Mario.KEY_SPEED] = false;//true
            trueJumpCounter = 0;
        }

        if (trueJumpCounter > 16)
        {
            trueJumpCounter = 0;
            action[Mario.KEY_JUMP] = false;
        }

        /*Little Jump*/
        if (((pos_front != 0 && pos_front != 2) && (pos_front_up == 0 || pos_front_up == 2) || pos_up == 2) || mob_front == 80 || mob_front == 93 || mob_front_down == 80 || mob_front_down == 93 || mob_2front == 80 || mob_2front == 93 || mob_2front_down == 80 || mob_2front_down == 93|| mob_2front_2down == 80 || mob_2front_2down == 93 || mob_3front_2down == 80 || mob_3front_2down == 93)
        {
            if (action[Mario.KEY_JUMP] == true)
                action[Mario.KEY_JUMP] = false;
            else
                action[Mario.KEY_JUMP] = true;
            jumpCounter = 1;
        }

        return action;
    }
}
