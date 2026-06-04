package controller;

import model.Position;
import model.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class PathFinder
{
    public List<Position> findShortestPath(
            Room room,
            Position start,
            Position target)
    {
        Queue<Position> queue =
                new LinkedList<>();

        Map<Position, Position> parentMap =
                new HashMap<>();

        Set<Position> visited =
                new HashSet<>();

        queue.add(start);
        visited.add(start);

        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        while(!queue.isEmpty())
        {
            Position current =
                    queue.poll();

            if(current.equals(target))
            {
                return buildPath(
                        parentMap,
                        start,
                        target
                );
            }

            for(int i = 0; i < 4; i++)
            {
                int newRow =
                        current.getRow() + dRow[i];

                int newCol =
                        current.getCol() + dCol[i];

                if(isValidMove(
                        room,
                        newRow,
                        newCol))
                {
                    Position next =
                            new Position(
                                    newRow,
                                    newCol
                            );

                    if(!visited.contains(next))
                    {
                        visited.add(next);

                        parentMap.put(
                                next,
                                current
                        );

                        queue.add(next);
                    }
                }
            }
        }

        return new ArrayList<>();
    }
    private boolean isValidMove(
            Room room,
            int row,
            int col)
    {
        if(row < 0 || row >= room.getRows())
            return false;

        if(col < 0 || col >= room.getCols())
            return false;

        if(room.getCell(row,col).isObstacle())
            return false;

        return true;
    }
    private List<Position> buildPath(
            Map<Position,Position> parentMap,
            Position start,
            Position target)
    {
        List<Position> path =
                new ArrayList<>();

        Position current = target;

        while(current != null)
        {
            path.add(current);

            if(current.equals(start))
                break;

            current =
                    parentMap.get(current);
        }

        Collections.reverse(path);

        return path;
    }

}
