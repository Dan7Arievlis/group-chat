import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Group {
  String name;
  Set<String> membersIds;

  public Group(String name) {
    this.name = name;
    this.membersIds = new HashSet<>();
  }

  public Set<String> getMembers() {
    return membersIds;
  }

  public boolean addMember(String memberId) {
    return membersIds.add(memberId);
  }

  public boolean removeMember(String groupName, String memberId) throws IOException {
    boolean value = membersIds.remove(memberId);
    if (membersIds.isEmpty()) {
      ClientHandler.groups.remove(name);
      return value;
    }

    Group group = ClientHandler.groups.get(groupName);
    for (String member : group.getMembers()) {
      ClientHandler user = ClientHandler.clientHandlers.get(member);
      if (!user.equals(memberId)) {
        user.writeMessage("WARNING" + Server.DELIMITER + groupName + Server.DELIMITER +
            "SERVER: " + ClientHandler.clientHandlers.get(memberId).getClientUserName() +
            " has left the chat.");
      }
    }

    return value;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Group other = (Group) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
