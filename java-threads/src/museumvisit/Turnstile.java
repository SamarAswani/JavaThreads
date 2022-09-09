package museumvisit;

import java.util.Optional;

public class Turnstile {

  private final MuseumSite originRoom;
  private final MuseumSite destinationRoom;

  public Turnstile(MuseumSite originRoom, MuseumSite destinationRoom) {
    assert !originRoom.equals(destinationRoom);
    this.originRoom = originRoom;
    this.destinationRoom = destinationRoom;
    originRoom.addExitTurnstile(this);
  }

  public Optional<MuseumSite> passToNextRoom() {
    MuseumSite firstRoomToLock;
    MuseumSite secondRoomToLock;
    if (destinationRoom.hasAvailability()) {
      if (originRoom.getName().compareTo(destinationRoom.getName()) < 0) {
        firstRoomToLock = originRoom;
        secondRoomToLock = destinationRoom;
      } else {
        firstRoomToLock = destinationRoom;
        secondRoomToLock = originRoom;
      }
      synchronized (firstRoomToLock) {
        synchronized (secondRoomToLock) {
          originRoom.exit();
          destinationRoom.enter();
        }
      }
      return Optional.of(destinationRoom);
    } else {
      return Optional.empty();
    }
  }

  public MuseumSite getOriginRoom() {
    return originRoom;
  }

  public MuseumSite getDestinationRoom() {
    return destinationRoom;
  }
}
