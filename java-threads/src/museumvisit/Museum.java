package museumvisit;

import java.util.*;
import java.util.stream.*;

public class Museum {

  private final Entrance entrance;
  private final Exit exit;
  private final Set<MuseumSite> sites;

  public Museum(Entrance entrance, Exit exit, Set<MuseumSite> sites) {
    this.entrance = entrance;
    this.exit = exit;
    this.sites = sites;
  }

  public static void main(String[] args) {
    final int numberOfVisitors = 100; // Your solution has to work with any
    // number of visitors
    final Museum museum = buildSimpleMuseum(); // buildLoopyMuseum();

    // create the threads for the visitors and get them moving
    Thread[] threads = new Thread[numberOfVisitors];

    Arrays.setAll(threads, t -> new Thread(new Visitor("visitor", museum.getEntrance())));
    Arrays.stream(threads).forEach(Thread::start);

    // wait for them to complete their visit

    Arrays.stream(threads)
        .forEach(
            t -> {
              try {
                t.join();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });

    // Checking no one is left behind
    if (museum.getExit().getOccupancy() == numberOfVisitors) {
      System.out.println("\nAll the visitors reached the exit\n");
    } else {
      System.out.println(
          "\n"
              + (numberOfVisitors - museum.getExit().getOccupancy())
              + " visitors did not reach the exit. Where are they?\n");
    }

    System.out.println("Occupancy status for each room (should all be zero, but the exit site):");
    museum
        .getSites()
        .forEach(
            s -> {
              System.out.println("Site " + s.getName() + " final occupancy: " + s.getOccupancy());
            });
  }

  private static Museum newMuseum() {
    Museum basicMuseum = new Museum(new Entrance(), new Exit(), new HashSet<MuseumSite>());
    basicMuseum.sites.add(basicMuseum.getEntrance());
    basicMuseum.sites.add(basicMuseum.getExit());
    return basicMuseum;
  }

  public static Museum buildSimpleMuseum() {
    Museum simpleMuseum = newMuseum();
    ExhibitionRoom exhibitionRoom = new ExhibitionRoom("Exhibition Room", 10);
    simpleMuseum.sites.add(exhibitionRoom);
    new Turnstile(simpleMuseum.getEntrance(), exhibitionRoom);
    new Turnstile(exhibitionRoom, simpleMuseum.getExit());

    return simpleMuseum;
  }

  public static Museum buildLoopyMuseum() {
    Museum loopMuseum = newMuseum();
    ExhibitionRoom venomRoom = new ExhibitionRoom("Venom KillerAndCure Room", 10);
    ExhibitionRoom whaleRoom = new ExhibitionRoom("Whales Exhibition Room", 10);
    new Turnstile(venomRoom, loopMuseum.getExit());
    new Turnstile(venomRoom, whaleRoom);
    new Turnstile(whaleRoom, venomRoom);
    loopMuseum.sites.add(venomRoom);
    loopMuseum.sites.add(whaleRoom);
    new Turnstile(loopMuseum.getEntrance(), venomRoom);
    return loopMuseum;
  }

  public Entrance getEntrance() {
    return entrance;
  }

  public Exit getExit() {
    return exit;
  }

  public Set<MuseumSite> getSites() {
    return sites;
  }
}
