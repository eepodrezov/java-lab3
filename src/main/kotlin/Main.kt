import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.text.SimpleDateFormat
import java.util.regex.Pattern

fun main() {
    val folderPath = "src/main/inputFiles" // Замените на путь к вашей папке
    val folder = File(folderPath)
    val logFile = File(folder, "rename_log.txt")

    if (!folder.exists() || !folder.isDirectory) {
        println("Папка не найдена!")
        return
    }

    val filePattern = Pattern.compile("""(\d{4})[-_](\d{2})[-_](\d{2})""")
    val files = folder.listFiles() ?: return
    var count = 1

    files.forEach { file ->
        if (file.isFile) {
            val matcher = filePattern.matcher(file.name)
            val dateStr = if (matcher.find()) {
                "${matcher.group(1)}-${matcher.group(2)}-${matcher.group(3)}"
            } else {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                sdf.format(file.lastModified())
            }

            val newFolder = File(folder, dateStr)
            if (!newFolder.exists()) newFolder.mkdir()

            val newFileName = "$dateStr (${count++}).${file.extension}"
            val newFile = File(newFolder, newFileName)

            Files.move(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
            logFile.appendText("Переименован: ${file.name} -> $newFileName\n")
        }
    }
    println("Переименование завершено. Проверьте лог-файл.")
}

