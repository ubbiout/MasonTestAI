//M. silent and closes game early  at turn <whatever is in the while loop on line 65> ....for debugging purposes
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
        
        int dieHere = 455;

        // Connect to the manager, starting the game
        GameController gc = new GameController();

        // Direction is a normal java enum.
        Direction[] directions = Direction.values();

        //M. trying not to break our server lol
        try {//M. just for testing purposes, the following code isn't smart and really only works on default map as blue
            
            while (gc.round() < dieHere) { //M. why can't this be an if statement?
                System.out.println("Current round: " + gc.round() + " let's go East");
                // VecUnit is a class that you can think of as similar to ArrayList<Unit>, but immutable.
                VecUnit units = gc.myUnits();
                for (int i = 0; i < units.size(); i++) {
                    Unit unit = units.get(i);

                    // Most methods on gc take unit IDs, instead of the unit objects themselves.
                    if (gc.isMoveReady(unit.id()) && gc.canMove(unit.id(), Direction.East)) {
                        gc.moveRobot(unit.id(), Direction.East);
                    }
                }
                gc.nextTurn(); //M. this is all we will do on this turn
            }

            System.out.println("Current round: " + gc.round() + " I got past everything!");
            // Submit the actions we've done, and wait for our next turn.
            gc.nextTurn(); //M. if we get to this point... end our turn. I don't know why this stops everything, but i don't want to crash server LOL OH GAWD

        } catch (Exception e) { //M. end of try()
            System.err.println("Exception caught: " + e.getMessage()); //gerald here
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