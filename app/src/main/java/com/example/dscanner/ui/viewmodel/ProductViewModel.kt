package com.example.dscanner.ui.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dscanner.data.local.dao.CompetitorsDao
import com.example.dscanner.data.local.entity.CompetitorsEntity
import com.example.dscanner.data.local.entity.ProductEditEntity
import com.example.dscanner.data.local.entity.ProductEntity
import com.example.dscanner.util.createBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val dao: CompetitorsDao
) : ViewModel() {

    val fixedProducts: StateFlow<List<ProductEntity>> =
        dao.getAllProductsFlow()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    private val _editedProducts = MutableStateFlow<List<ProductEditEntity>>(emptyList())

    private val _selectedCompetitorId = MutableStateFlow<Long?>(null)
    val selectedCompetitorId: StateFlow<Long?> = _selectedCompetitorId

    private val _barCodeImage = mutableStateOf<Bitmap?>(null)
    val barCodeImage: State<Bitmap?> = _barCodeImage

    val competitors: StateFlow<List<CompetitorsEntity>> =
        dao.listCompetitorsFlow()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val defaultProducts = listOf(
        ProductEntity(name = "ARROZ SEPE T1 5KG", barCode = "7896009865657"),
        ProductEntity(name = "ARROZ SEPE PARB T1 5KG", barCode = "7896009867293"),
        ProductEntity(name = "ARROZ SEPE PREMIUM T1 5KG", barCode = "7896009866944"),
        ProductEntity(name = "ARROZ PURO GRÃO BCO T1 5KG", barCode = "7897297900112"),
        ProductEntity(name = "ARROZ SABOROSO CAAL T1 5KG", barCode = "7898295150189"),
        ProductEntity(name = "ARROZ ALEGRE T1 5KG", barCode = "7898295155207"),
        ProductEntity(name = "ARROZ RAMPINELLI BCO LF 5KG", barCode = "7896546112252"),
        ProductEntity(name = "ARROZ RAMPINELLI PARB 5KG", barCode = "7896546110050"),
        ProductEntity(name = "ARROZ DONO DA CASA T1 5KG", barCode = "7896641000317"),
        ProductEntity(name = "ARROZ SEPE BCO T1 1KG", barCode = "7896009863547"),
        ProductEntity(name = "ARROZ SEPE T1 2KG", barCode = "7896009863752"),
        ProductEntity(name = "ARROZ SEPE PARB T1 1KG", barCode = "7896009866050"),
        ProductEntity(name = "ARROZ RAMPINELLI BCO T1 1KG", barCode = "7896546112214"),
        ProductEntity(name = "ARROZ RAMPINELLI PARB T1 1KG", barCode = "7896546110012"),
        ProductEntity(name = "ARROZ CALAFATE T1 1KG", barCode = "7896010355062"),
        ProductEntity(name = "FEIJÃO PRETO COMBRASIL 1KG", barCode = "7896101062015"),
        ProductEntity(name = "FEIJÃO PRETO ANA PLUS T1 1KG", barCode = "7898063330072"),
        ProductEntity(name = "FEIJÃO PRETO RIO DOCE 1KG", barCode = "7897095900758"),
        ProductEntity(name = "FEIJÃO PRETO JUJU T1 1KG", barCode = "7896517400036"),
        ProductEntity(name = "FEIJÃO PRETO NORTE T1 1KG", barCode = "7898052521160"),
        ProductEntity(name = "FEIJÃO PRETO NICO T1 1KG", barCode = "7897769200108"),
        ProductEntity(name = "FEIJÃO PRETO LÍVIA T1 1KG", barCode = "7897769200320"),
        ProductEntity(name = "FEIJÃO CARIOCA COMBRASIL 1KG", barCode = "7896101062022"),
        ProductEntity(name = "FEIJÃO CARIOCA ANA PLUS T1 1KG", barCode = "7898063330041"),
        ProductEntity(name = "FEIJÃO CARIOCA JUJU T1 1KG", barCode = "7896517400302"),
        ProductEntity(name = "FEIJÃO CARIOCA NORTE T1 1KG", barCode = "7898052521016"),
        ProductEntity(name = "FEIJÃO CARIOCA NICO T1 1KG", barCode = "7897769200184"),
        ProductEntity(name = "FEIJÃO CARIOCA LÍVIA T1 1KG", barCode = "7897769200290"),
        ProductEntity(name = "AÇÚCAR CRISTAL ALCON 5KG", barCode = "7898996103330"),
        ProductEntity(name = "AÇÚCAR CRISTAL SANTA ISABEL 5KG", barCode = "7898187830052"),
        ProductEntity(name = "AÇÚCAR CRISTAL CARAVELAS 5KG", barCode = "7896894900082"),
        ProductEntity(name = "AÇÚCAR CRISTAL COAGRO 5KG", barCode = "7898903106010"),
        ProductEntity(name = "AÇÚCAR CRISTAL ALCON 2KG", barCode = "7898996103323"),
        ProductEntity(name = "AÇÚCAR CRISTAL SANTA ISABEL 2KG", barCode = "7898187830021"),
        ProductEntity(name = "AÇÚCAR CRISTAL CARAVELAS 2KG", barCode = "7896894900075"),
        ProductEntity(name = "AÇÚCAR CRISTAL COAGRO 2KG", barCode = "7898903106027"),
        ProductEntity(name = "ÓLEO DE SOJA LIZA PET 900ML", barCode = "7896036090244"),
        ProductEntity(name = "ÓLEO DE SOJA ABC PET 900ML", barCode = "7896242200048"),
        ProductEntity(name = "ÓLEO DE SOJA SOYA PET 900ML", barCode = "7891080801204"),
        ProductEntity(name = "ÓLEO DE SOJA COAMO PET 900ML", barCode = "7896279600538"),
        ProductEntity(name = "ÓLEO DE SOJA VITALIV PET 900ML", barCode = "7898247780297"),
        ProductEntity(name = "LEITE LONGA VIDA INT CAPEL 1L", barCode = "7898360430215"),
        ProductEntity(name = "LEITE LONGA VIDA INT DAMARE 1L", barCode = "7898945133203"),
        ProductEntity(name = "LEITE LONGA VIDA INT ITALAC 1L", barCode = "7898080640611"),
        ProductEntity(name = "LEITE LONGA VIDA INT PIRACANJUBA 1L", barCode = "7898215151708"),
        ProductEntity(name = "LEITE LONGA VIDA INT PORTO ALEGRE 1L", barCode = "7896122305207"),
        ProductEntity(name = "LEITE LONGA VIDA INT SELITA 1L", barCode = "7896484411509"),
        ProductEntity(name = "CAFÉ 3 CORAÇÕES ALMOF 250G EXTRA FORTE", barCode = "7896005801512"),
        ProductEntity(name = "CAFÉ FORT POUCH 250G TORRADO E MOÍDO", barCode = "7896005800539"),
        ProductEntity(name = "CAFÉ NÚMERO UM ALMOF 250G TRADICIONAL", barCode = "7896212700011"),
        ProductEntity(name = "CAFÉ CAFUSO ALMOF 250G TRADICIONAL", barCode = "7896298600113"),
        ProductEntity(name = "CAFÉ PILÃO VACUO PURO 250G TRADICIONAL", barCode = "7896089012637"),
        ProductEntity(name = "CAFÉ MERIDIANO ALMOF 250G EXTRA FORTE", barCode = "7896246700162"),
        ProductEntity(name = "CAFÉ MERIDIANO ALMOF 250G TRADICIONAL", barCode = "7896246700025"),
        ProductEntity(name = "CAFÉ GRÃO DA ROÇA ALMOF 250G TRADICIONAL", barCode = "7898287600029"),
        ProductEntity(name = "CAFÉ GLÓRIA ALMOF 250G EXTRA FORTE", barCode = "7898908355192"),
        ProductEntity(name = "CAFÉ DAMARE ALMOF 250G TRADICIONAL", barCode = "7898945133012"),
        ProductEntity(name = "CAFÉ MELITTA POUCH 250G TRADICIONAL", barCode = "7891021006088"),
        ProductEntity(name = "FARINHA TRIGO ADRIA PP 1KG", barCode = ""),

        ProductEntity(name = "FARINHA TRIGO ESP PALOMA 1KG", barCode = ""),

        ProductEntity(name = "FARINHA TRIGO NUMERO UM 5KG", barCode = ""),

        ProductEntity(name = "FARINHA TRIGO NUMERO UM FERM 1KG", barCode = ""),

        ProductEntity(name = "FARINHA TRIGO NUMERO UM POLI 1KG", barCode = ""),

        ProductEntity(name = "FARINHA TRIGO NUMERO UM PP 1KG", barCode = ""),

        ProductEntity(name = "FARINHA TRIGO REGINA 5KG", barCode = ""),

        ProductEntity(name = "FARINHA TRIGO REGINA INTEGRAL 1KG", barCode = ""),

        ProductEntity(name = "FARINHA TRIGO REGINA PP 1KG", barCode = ""),

        ProductEntity(name = "FARINHA TRIGO REGINA SEMOLADA 25KG", barCode = ""),

        ProductEntity(name = "FARINHA TRIGO TRES COROAS T1 1KG", barCode = ""),

        ProductEntity(name = "LEITE LV INT BH 1L", barCode = ""),

        ProductEntity(name = "ARROZ CATARINAO BCO T1 5KG", barCode = ""),

        ProductEntity(name = "ARROZ DONA MILU T1 5KG", barCode = ""),

        ProductEntity(name = "ARROZ PURO GRAO BCO T1 1KG", barCode = ""),

        ProductEntity(name = "ARROZ SEPE PREMIUM T1 1KG", barCode = ""),

        ProductEntity(name = "OLEO SOJA ABC PET 900ML", barCode = ""),

        )

    init {
        viewModelScope.launch {
            if (!dao.hasAnyProducts()) {
                dao.insertAllFixedProducts(defaultProducts)
            }
        }
    }

    fun saveProductForCompetitor(product: ProductEntity, price: Double, competitorId: Long) {
        viewModelScope.launch {
            val edit = ProductEditEntity(
                name = product.name,
                barCode = product.barCode,
                price = price,
                competitorId = competitorId
            )
            dao.insertProductEdit(edit)
            _editedProducts.value = dao.getEditedProductsForCompetitor(competitorId)
        }
    }

    fun createCompetitor(name: String) {
        viewModelScope.launch {
            val id = dao.insertCompetitor(CompetitorsEntity(name = name))
            _selectedCompetitorId.value = id
            // 👇 Atualiza os concorrentes, se necessário (pode ser omitido se usar `Flow`)
            // _competitors.value = dao.getAllCompetitors()
            // Mas como você já usa `listCompetitorsFlow()`, ele atualiza automaticamente
        }
    }

    fun selectCompetitor(id: Long) {
        _selectedCompetitorId.value = id
    }

    fun addProduct(
        name: String,
        barCode: String
    ) {
        viewModelScope.launch {
            dao.insertProduct(
                ProductEntity(
                    name = name,
                    barCode = barCode
                )
            )
        }
    }

    fun createBarCodeImage(code: String) {
        _barCodeImage.value = createBar(code)
    }

    val competitorsWithProducts =
        dao.getAllCompetitorsWithProductsFlow()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )
}

