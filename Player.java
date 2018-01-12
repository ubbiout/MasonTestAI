//M. silent and closes game early  at turn 15, whatever is in the while loop on line 65 ....for debugging purposes
// import the API.
import bc.*;

public class Player {
    public static void main(String[] args) {
        // MapLocation is a data structure you'll use a lot.
        MapLocation loc = new MapLocation(Planet.Earth, 10, 20);
        //System.out.println("loc: " + loc + ", one step to the Northwest: " + loc.add(Direction.Northwest));
        //System.out.println("loc x: " + loc.getX());

        // One slightly weird thing: some methods are currently static methods on a static class called bc.
        // This will eventually be fixed :/
        //System.out.println("Opposite of " + Direction.East + ": " + bc.bcDirectionOpposite(Direction.East));

        // Connect to the manager, starting the game
        GameController gc = new GameController();

        // Direction is a normal java enum.
        Direction[] directions = Direction.values();

        //M. trying not to break our server lol
        try {//M. just for testing purposes, the following code isn't smart and really only works on default map as blue
            //M. let's do some rocket research!
            if (gc.round() == 1) {
                //System.out.println("Current round: " + gc.round() + " let's research a rocket!");
                gc.queueResearch(UnitType.Rocket); //M. research some rocket!
            }

            while (gc.round() == 1) { //M. why can't this be an if statement?
                //System.out.println("Current round: " + gc.round());
                // VecUnit is a class that you can think of as similar to ArrayList<Unit>, but immutable.
                VecUnit units = gc.myUnits();
                for (int i = 0; i < units.size(); i++) {
                    Unit unit = units.get(i);

                    // Most methods on gc take unit IDs, instead of the unit objects themselves.
                    if (gc.isMoveReady(unit.id()) && gc.canMove(unit.id(), Direction.West)) {
                        gc.moveRobot(unit.id(), Direction.West);
                    }
                }
                gc.nextTurn(); //M. this is all we will do on turn 1
            }
            while (gc.round() >= 2 && gc.round() <= 8) { //M. let's gc.replicate(int worker_id, Direction direction)
                //System.out.println("Current round: " + gc.round() + " let's replicate to the west");
                // VecUnit is a class that you can think of as similar to ArrayList<Unit>, but immutable.
                VecUnit units = gc.myUnits();
                for (int i = 0; i < units.size(); i++) {
                    Unit unit = units.get(i);
                    if (gc.canReplicate(unit.id(), Direction.West)) { //M. canReplicate: Whether the worker is ready to replicate. Tests that the worker's ability heat is sufficiently low, that the team has sufficient karbonite in its resource pool, and that the square in the given direction is empty.
                        //System.out.println("Unit replicating: " + unit.id()); //M.
                        try {
                            //System.out.println("Replican(t)!");
                            gc.replicate(unit.id(), Direction.West);
                        } catch (Exception e) { //M. end of try()
                            //System.err.println("Exception caught: " + e.getMessage()); //gerald here
                            e.printStackTrace();
                        }//M. end of catch()
                    }
                }
                gc.nextTurn(); //M. this is all we will do on turn 1-5
            }

            //build a factory on round 9+
            while (gc.round() > 8 && gc.round() < 15) { //M. just build factories forever lol... on the same square
                //System.out.println("Current round: " + gc.round() + " let's build a factory with " + gc.karbonite() + "karbonite in the bank");
                VecUnit units = gc.myUnits();
                for (int i = 0; i < units.size(); i++) { //M. all the workers/units will try to do the same thing
                    Unit unit = units.get(i);
                    if (gc.karbonite() >= (300 / 4)) { //M. 300/4 is blueprint cost of factory. factory max hp/4
                        //System.out.println("Unit constructing Blueprint: " + unit.id());
                        //if (gc.canBlueprint(unit.id(), UnitType.Worker, Direction.South)) { //M. this fucking stops us
                            //System.out.println("Unit: " + unit.id() + " attempts to construct");
                            try {
                                //System.out.println("Blueprint exists!");
                                gc.blueprint(unit.id(), UnitType.Factory, Direction.South);
                            } catch (Exception e) { //M. end of try()
                                //System.err.println("Exception caught: " + e.getMessage()); //gerald here
                                e.printStackTrace();
                            }//M. end of catch()
                        //}
                    }
                    if(unit.maxHealth() == 300) { //300 is factory maxHealth... so it's a factory?
                        //System.out.println("Unit " + unit.id() + "is a factory!");

                        //if(gc.canBuild(unit.id(), need blueprint_id here)){ //M.idk what a blueprint_id is

                        //}
                    }
                    if(unit.maxHealth() == 100) { //300 is worker and healer maxHealth... so it's a worker?
                        //System.out.println("Unit " + unit.id() + "is a worker!");
                        //Location site = unit.location();
                        //Unit aFactory = gc.senseUnitAtLocation(); //senseUnitAtLocation(MapLocation location)

                        //if(gc.canBuild(unit.id(), need blueprint_id here)){ //M.idk what a blueprint_id is

                        //}
                    }
                }
                // Submit the actions we've done, and wait for our next turn.
                gc.nextTurn(); //M. if we get to this point... end our turn
            }
            //System.out.println("Current round: " + gc.round() + " I got past everything!");
            // Submit the actions we've done, and wait for our next turn.
            gc.nextTurn(); //M. if we get to this point... end our turn. I don't know why this stops everything, but i don't want to crash server LOL OH GAWD

        } catch (Exception e) { //M. end of try()
            //System.err.println("Exception caught: " + e.getMessage()); //gerald here
            e.printStackTrace();
        }//M. end of catch()


    }//M. end of main()
} //M. end of Player



/* looks like the only map right now has no deposit on it LOL
public location locateInitialKarboniteDeposits(){
  //M. loop through bc.startingMap(bc.Planet.Earth) and check each with initialKarboniteAt(x and y) or probably not bc.karboniteAt(MapLocation location)
  //initialize vector of locations here
    VecMapLocation karbLoc;
  for (int i = 0; i < bc.startingMap(bc.Planet.Earth).width(); i++) {
     for (int j = 0; j < bc.startingMap(bc.Planet.Earth).height(); i++) {
       if(initialKarboniteAt(i,j) > 0){
         //save location into vector
       }
  }
  //return vector of locations with karbonite deposits
}

adjenda:
research rockets
build a factory blueprint
build the factory
build workers, or replicate workers
build a rocket blueprint
build the rocket
load workers into the rocket garrison
fly the rocket to mars
unload units from rocket garrison

how to write a mars player:
not sure but Player.java runs on mars

*/