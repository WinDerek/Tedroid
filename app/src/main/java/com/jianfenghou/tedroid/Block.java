package com.jianfenghou.tedroid;


import java.util.ArrayList;
import java.util.List;

public class Block {
    public static List<Coordinate> getRelativeCoordinates(int type, int rotateTimes) {
        List<Coordinate> relativeCoordinates = new ArrayList<Coordinate>();

        switch (type) {
            case 0:
                relativeCoordinates.add(new Coordinate(0, 0));
                relativeCoordinates.add(new Coordinate(0, 1));
                relativeCoordinates.add(new Coordinate(1, 0));
                relativeCoordinates.add(new Coordinate(1, 1));

                break;

            case 1:
                switch (rotateTimes) {
                    case 0:
                        relativeCoordinates.add(new Coordinate(0, 2));
                        relativeCoordinates.add(new Coordinate(1, 0));
                        relativeCoordinates.add(new Coordinate(1, 1));
                        relativeCoordinates.add(new Coordinate(1, 2));

                        break;

                    case 1:
                        relativeCoordinates.add(new Coordinate(0, 0));
                        relativeCoordinates.add(new Coordinate(0, 1));
                        relativeCoordinates.add(new Coordinate(1, 1));
                        relativeCoordinates.add(new Coordinate(2, 1));

                        break;

                    case 2:
                        relativeCoordinates.add(new Coordinate(0, 0));
                        relativeCoordinates.add(new Coordinate(0, 1));
                        relativeCoordinates.add(new Coordinate(0, 2));
                        relativeCoordinates.add(new Coordinate(1, 0));

                        break;

                    case 3:
                        relativeCoordinates.add(new Coordinate(0, 0));
                        relativeCoordinates.add(new Coordinate(1, 0));
                        relativeCoordinates.add(new Coordinate(2, 0));
                        relativeCoordinates.add(new Coordinate(2, 1));

                        break;

                    default:
                        break;
                }

                break;

            case 2:
                switch (rotateTimes) {
                    case 0:
                        relativeCoordinates.add(new Coordinate(0, 0));
                        relativeCoordinates.add(new Coordinate(1, 0));
                        relativeCoordinates.add(new Coordinate(1, 1));
                        relativeCoordinates.add(new Coordinate(1, 2));

                        break;

                    case 1:
                        relativeCoordinates.add(new Coordinate(0, 1));
                        relativeCoordinates.add(new Coordinate(1, 1));
                        relativeCoordinates.add(new Coordinate(2, 0));
                        relativeCoordinates.add(new Coordinate(2, 1));

                        break;

                    case 2:
                        relativeCoordinates.add(new Coordinate(0, 0));
                        relativeCoordinates.add(new Coordinate(0, 1));
                        relativeCoordinates.add(new Coordinate(0, 2));
                        relativeCoordinates.add(new Coordinate(1, 2));

                        break;

                    case 3:
                        relativeCoordinates.add(new Coordinate(0, 0));
                        relativeCoordinates.add(new Coordinate(0, 1));
                        relativeCoordinates.add(new Coordinate(1, 0));
                        relativeCoordinates.add(new Coordinate(2, 0));

                        break;

                    default:
                        break;
                }

                break;

            case 3:
                switch (rotateTimes) {
                    case 0:case 2:
                        relativeCoordinates.add(new Coordinate(0, 1));
                        relativeCoordinates.add(new Coordinate(0, 2));
                        relativeCoordinates.add(new Coordinate(1, 0));
                        relativeCoordinates.add(new Coordinate(1, 1));

                        break;

                    case 1:case 3:
                        relativeCoordinates.add(new Coordinate(0, 0));
                        relativeCoordinates.add(new Coordinate(1, 0));
                        relativeCoordinates.add(new Coordinate(1, 1));
                        relativeCoordinates.add(new Coordinate(2, 1));

                        break;

                    default:
                        break;
                }

                break;

            case 4:
                switch (rotateTimes) {
                    case 0:case 2:
                        relativeCoordinates.add(new Coordinate(0, 0));
                        relativeCoordinates.add(new Coordinate(0, 1));
                        relativeCoordinates.add(new Coordinate(1, 1));
                        relativeCoordinates.add(new Coordinate(1, 2));

                        break;

                    case 1:case 3:
                        relativeCoordinates.add(new Coordinate(0, 1));
                        relativeCoordinates.add(new Coordinate(1, 0));
                        relativeCoordinates.add(new Coordinate(1, 1));
                        relativeCoordinates.add(new Coordinate(2, 0));

                        break;

                    default:
                        break;
                }

                break;

            case 5:
                switch (rotateTimes) {
                    case 0:case 2:
                        relativeCoordinates.add(new Coordinate(0, 0));
                        relativeCoordinates.add(new Coordinate(0, 1));
                        relativeCoordinates.add(new Coordinate(0, 2));
                        relativeCoordinates.add(new Coordinate(0, 3));

                        break;

                    case 1:case 3:
                        relativeCoordinates.add(new Coordinate(0, 0));
                        relativeCoordinates.add(new Coordinate(1, 0));
                        relativeCoordinates.add(new Coordinate(2, 0));
                        relativeCoordinates.add(new Coordinate(3, 0));

                        break;

                    default:
                        break;
                }

                break;

            case 6:
                switch (rotateTimes) {
                    case 0:
                        relativeCoordinates.add(new Coordinate(0, 1));
                        relativeCoordinates.add(new Coordinate(1, 0));
                        relativeCoordinates.add(new Coordinate(1, 1));
                        relativeCoordinates.add(new Coordinate(1, 2));

                        break;

                    case 1:
                        relativeCoordinates.add(new Coordinate(0, 1));
                        relativeCoordinates.add(new Coordinate(1, 0));
                        relativeCoordinates.add(new Coordinate(1, 1));
                        relativeCoordinates.add(new Coordinate(2, 1));

                        break;

                    case 2:
                        relativeCoordinates.add(new Coordinate(0, 0));
                        relativeCoordinates.add(new Coordinate(0, 1));
                        relativeCoordinates.add(new Coordinate(0, 2));
                        relativeCoordinates.add(new Coordinate(1, 1));

                        break;

                    case 3:
                        relativeCoordinates.add(new Coordinate(0, 0));
                        relativeCoordinates.add(new Coordinate(1, 0));
                        relativeCoordinates.add(new Coordinate(1, 1));
                        relativeCoordinates.add(new Coordinate(2, 0));

                        break;

                    default:
                        break;
                }

                break;

            default:
                break;
        }

        return relativeCoordinates;
    }
}
