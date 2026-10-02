import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Beginner Task 4: File handling — read/write text files using
 * classic Java IO (RandomAccessFile) and modern Java NIO (Files).
 */
public class FileHandlingDemo {
    public static void main(String[] args) throws IOException {
        Path niopath = Path.of("nio_output.txt");
        Path iopath = Path.of("io_output.txt");

        // ---- Writing & reading with java.nio.file.Files (recommended, modern API) ----
        List<String> lines = List.of(
                "Line 1: Hello from java.nio",
                "Line 2: File handling demo",
                "Line 3: internSpark Java Developer task"
        );
        Files.write(niopath, lines, StandardCharsets.UTF_8);
        System.out.println("Wrote " + lines.size() + " lines to " + niopath);

        List<String> readBack = Files.readAllLines(niopath, StandardCharsets.UTF_8);
        System.out.println("\n--- Contents read via NIO (Files.readAllLines) ---");
        readBack.forEach(System.out::println);

        // Appending with NIO
        Files.writeString(niopath, "\nLine 4: appended via Files.writeString",
                StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
        System.out.println("\nAppended a line via NIO.");

        // ---- Writing & reading with classic java.io (RandomAccessFile) ----
        try (RandomAccessFile raf = new RandomAccessFile(iopath.toFile(), "rw")) {
            raf.writeBytes("Hello from classic java.io RandomAccessFile\n");
            raf.writeBytes("Second line written via java.io\n");
        }
        System.out.println("\nWrote content to " + iopath + " via java.io.RandomAccessFile");

        System.out.println("\n--- Contents read via java.io (RandomAccessFile) ---");
        try (RandomAccessFile raf = new RandomAccessFile(iopath.toFile(), "r")) {
            String line;
            while ((line = raf.readLine()) != null) {
                System.out.println(line);
            }
        }

        // Clean-up note: files are left on disk intentionally so they can be
        // shown in a screenshot for the submission.
        System.out.println("\nDone. Files created: " + niopath.toAbsolutePath()
                + " , " + iopath.toAbsolutePath());
    }
}
