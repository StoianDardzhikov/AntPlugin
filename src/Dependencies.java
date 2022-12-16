import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Dependencies extends Task {
    Path localRepo = Path.of("C:/apache-ant-1.9.16/repo");
    Path libDir = Path.of("./lib");
    String mavenRepo = "https://repo1.maven.org/maven2";
    HttpClient client = HttpClient.newHttpClient();
    List<Dependency> dependencies = new ArrayList<>();

    public void addConfiguredDependency(Dependency d) {
        dependencies.add(d);
    }

    public void execute() throws BuildException {
        for (Dependency dependency : dependencies) {
            String groupId = dependency.groupId.text;
            String artifactId = dependency.artifactId.text;
            String version = dependency.version.text;
            String jarName =  artifactId + "-" + version + ".jar";
            Path existingJarPath = libDir.resolve(jarName);
            if (Files.exists(existingJarPath))
                continue;

            String jarDest = "/" + groupId + "/" + artifactId + "/" + version + "/" + jarName;
            String src = mavenRepo + jarDest;
            try {
                if (Files.notExists(localRepo)) {
                    Files.createDirectory(localRepo);
                }

                Path jarPath = localRepo.resolve(jarName);
                if (Files.notExists(jarPath)) {
                    System.out.println("Downloading " + jarName);
                    HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(new URI(src)).build();
                    client.send(httpRequest, HttpResponse.BodyHandlers.ofFile(jarPath));
                }

                Path libJarPath = libDir.resolve(jarName);
                System.out.println("Copying " + jarName + " to " + libJarPath);
                Files.copy(jarPath, libJarPath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}