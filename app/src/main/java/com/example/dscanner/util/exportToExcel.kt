import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.dscanner.data.local.entity.CompetitorWithProducts
import com.example.dscanner.data.local.entity.ProductEditEntity
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

fun exportToExcel(
    context: Context,
    competitorName: String,
    products: List<ProductEditEntity>
): File? {
    return try {

        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Produtos")

        // Cabeçalhos
        val header = sheet.createRow(0)
        header.createCell(0).setCellValue("Nome")
        header.createCell(1).setCellValue("Código de Barras")
        header.createCell(2).setCellValue("Preço")

        // Dados
        products.forEachIndexed { index, product ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(product.name)
            row.createCell(1).setCellValue(product.barCode)
            row.createCell(2).setCellValue(product.price)
        }

        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val exportDir = File(downloadsDir, "DScannerExports2026")

        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        val safeCompetitorName =
            competitorName.replace(Regex("[^a-zA-Z0-9_]"), "_")

        val file = File(
            exportDir,
            "produtos_$safeCompetitorName.xlsx"
        )

        FileOutputStream(file).use {
            workbook.write(it)
        }

        workbook.close()

        Toast.makeText(
            context,
            "Excel salvo em: ${file.absolutePath}",
            Toast.LENGTH_LONG
        ).show()

        file

    } catch (e: Exception) {

        Toast.makeText(
            context,
            "Erro ao exportar: ${e.localizedMessage}",
            Toast.LENGTH_LONG
        ).show()

        null
    }
}

fun exportAllToExcel(
    context: Context,
    competitors: List<CompetitorWithProducts>
): File? {

    return try {

        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Pesquisa de Preços")

        // Todos os produtos existentes
        val allProducts = competitors
            .flatMap { it.products }
            .map { it.name }
            .distinct()
            .sorted()

        // Cabeçalhos
        val header = sheet.createRow(0)

        header.createCell(0).setCellValue("Produto")

        competitors.forEachIndexed { index, competitor ->
            header.createCell(index + 1).setCellValue(
                competitor.competitor.name
            )
        }

        // Preenche os produtos
        allProducts.forEachIndexed { rowIndex, productName ->

            val row = sheet.createRow(rowIndex + 1)

            // Nome do produto
            row.createCell(0).setCellValue(productName)

            // Preço em cada concorrente
            competitors.forEachIndexed { columnIndex, competitor ->

                val product = competitor.products.find {
                    it.name == productName
                }

                if (product != null) {
                    row.createCell(columnIndex + 1)
                        .setCellValue(product.price)
                } else {
                    row.createCell(columnIndex + 1)
                        .setCellValue("")
                }
            }
        }

        // Larguras das colunas
        sheet.setColumnWidth(0, 15000)

        competitors.indices.forEach { index ->
            sheet.setColumnWidth(index + 1, 5000)
        }

        // Pasta de exportação
        val downloadsDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        )

        val exportDir = File(downloadsDir, "DScannerExports")

        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        val file = File(
            exportDir,
            "Pesquisa_de_Precos.xlsx"
        )

        FileOutputStream(file).use {
            workbook.write(it)
        }

        workbook.close()

        Toast.makeText(
            context,
            "Excel salvo em: ${file.absolutePath}",
            Toast.LENGTH_LONG
        ).show()

        file

    } catch (e: Exception) {

        Toast.makeText(
            context,
            "Erro ao exportar: ${e.localizedMessage}",
            Toast.LENGTH_LONG
        ).show()

        e.printStackTrace()

        null
    }
}

fun shareFile(
    context: Context,
    file: File
) {
    try {

        val authority = context.packageName + ".provider"

        val uri: Uri = FileProvider.getUriForFile(
            context,
            authority,
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {

            type =
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"

            putExtra(Intent.EXTRA_STREAM, uri)

            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(
            Intent.createChooser(
                shareIntent,
                "Compartilhar arquivo Excel"
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )

    } catch (e: Exception) {

        Toast.makeText(
            context,
            "Erro ao compartilhar arquivo: ${e.localizedMessage}",
            Toast.LENGTH_LONG
        ).show()
    }
}