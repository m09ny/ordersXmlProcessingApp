package sacom.orders.ordersTool;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import sacom.orders.processingFiles.ReadOrderXmlFile;

@Component
public class OrdersWatchService {

	private final WatchService watcher;
	private final Map<WatchKey, Path> keys;

	/**
	 * Creates a WatchService and registers the given directory
	 */
	OrdersWatchService() throws IOException {
		this.watcher = FileSystems.getDefault().newWatchService();
		this.keys = new HashMap<WatchKey, Path>();

	}

	OrdersWatchService(Path dir) throws IOException {
		this.watcher = FileSystems.getDefault().newWatchService();
		this.keys = new HashMap<WatchKey, Path>();

		walkAndRegisterDirectories(dir);
	}

	/**
	 * Register the given directory with the WatchService; This function will be
	 * called by FileVisitor
	 */
	private void registerDirectory(Path dir) throws IOException {
		WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		keys.put(key, dir);
	}

	/**
	 * Register the given directory, and all its sub-directories, with the
	 * WatchService.
	 */
	private void walkAndRegisterDirectories(final Path start) throws IOException {
		// register directory and sub-directories
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				registerDirectory(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * Process all events for keys queued to the watcher
	 */
	void processEvents() {
		for (;;) {

			// wait for key to be signalled
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				return;
			}

			Path dir = keys.get(key);
			if (dir == null) {
				System.err.println("WatchKey not recognized!!");
				continue;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
				@SuppressWarnings("rawtypes")
				WatchEvent.Kind kind = event.kind();

				// Context for directory entry event is the file name of entry
				@SuppressWarnings("unchecked")
				Path name = ((WatchEvent<Path>) event).context();
				Path child = dir.resolve(name);

				if (kind == ENTRY_CREATE) {
					// print out event

					File file = new File(child.toString());

					if (file.exists() && file.isFile()) {
						Path pathChild = Paths.get(child.toString()).toAbsolutePath();
						Path pathOrders = Paths.get("../orders/src/main/resources/orders/").toAbsolutePath();

						if (pathChild.startsWith(pathOrders)) {
							System.out.format("%s: %s\n", event.kind().name(), child);

							if (checkValidFileName(
									file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\") + 1))) {
								new ReadOrderXmlFile().getFileNameFromDir(file);
							} else {
								System.out.println(
										file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\") + 1)
												+ " is not a valid file name, please rename it!");
							}

						}

					}
				}
			}

			// reset key and remove from set if directory no longer accessible
			boolean valid = key.reset();
			if (!valid) {
				keys.remove(key);

				// all directories are inaccessible
				if (keys.isEmpty()) {
					break;
				}
			}
		}
	}

	private boolean checkValidFileName(String fileName) {

		Pattern pattern = Pattern.compile(".[a-zA-Z]+\\d{1,2}\\.xml$");
		return pattern.matcher(fileName).matches();
	}

	public void watchFolder() throws IOException {
		Path dir = Paths.get("../orders");

		new OrdersWatchService(dir).processEvents();
	}
}