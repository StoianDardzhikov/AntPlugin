public class Dependency {

    GroupId groupId;
    ArtifactId artifactId;
    Version version;

    public Dependency () {}

    public void addConfiguredGroupId(GroupId groupId) {
        this.groupId = groupId;
    }

    public void addConfiguredArtifactId(ArtifactId artifactId) {
        this.artifactId = artifactId;
    }

    public void addConfiguredVersion(Version version) {
        this.version = version;
    }
}