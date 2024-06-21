
package ar.edu.unlam.mobile.scaffold.ui.screens.profile

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ar.edu.unlam.mobile.scaffold.domain.model.ActivityLevel
import ar.edu.unlam.mobile.scaffold.domain.model.Gender
import ar.edu.unlam.mobile.scaffold.domain.model.GoalType
import ar.edu.unlam.mobile.scaffold.domain.model.UserInfo
import ar.edu.unlam.mobile.scaffold.navigation.Route
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val profileImage by viewModel.profileImage.collectAsState()
    val userInfo by viewModel.userInfo.observeAsState()

    LaunchedEffect(navController) {
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Uri>("imageUri")
            ?.observe(lifecycleOwner) { uri ->
                uri?.let {
                    val bitmap = uriToBitmap(context, it)
                    bitmap?.let { bmp ->
                        viewModel.updateProfileImage(bmp)
                    }
                }
            }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0E0E0))
    ) {
        HeaderSection(navController, viewModel)
        BodySection(viewModel, userInfo)
    }
}
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HeaderSection(navController: NavHostController, viewModel: ProfileViewModel = hiltViewModel(),) {
    val profileImage by viewModel.profileImage.collectAsState()
    val lifecycle = LocalLifecycleOwner.current
    val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    var permissionsGranted by remember { mutableStateOf(false) }

    // Observa el estado de los permisos
    LaunchedEffect(permissionState) {
        permissionsGranted = permissionState.status.isGranted
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primaryVariant)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            if (profileImage != null) {
                Image(
                    bitmap = profileImage!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(80.dp)
                )
            }

            IconButton(
                onClick = {
                    // Solicitar permisos cuando se toca el icono de edición
                    permissionState.launchPermissionRequest()
                    if (permissionState.status.isGranted) {
                        navController.navigate(Route.CAMERA)
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(12.dp, (-12).dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Florencia Santamaría", style = MaterialTheme.typography.h6, color = Color.White)
    }
}

@Composable
fun BodySection(viewModel: ProfileViewModel, userInfo: UserInfo?) {
    var isEditing by remember { mutableStateOf(false) }
    var age by remember { mutableStateOf(TextFieldValue(userInfo?.age?.toString() ?: "")) }
    var weight by remember { mutableStateOf(TextFieldValue(userInfo?.weight?.toString() ?: "")) }
    var height by remember { mutableStateOf(TextFieldValue(userInfo?.height?.toString() ?: "")) }
    var gender by remember { mutableStateOf(TextFieldValue(userInfo?.gender?.name ?: "")) }
    var activityLevel by remember { mutableStateOf(TextFieldValue(userInfo?.activityLevel?.name ?: "")) }
    var goalType by remember { mutableStateOf(TextFieldValue(userInfo?.goalType?.name ?: "")) }
    var carbRatio by remember { mutableStateOf(TextFieldValue(userInfo?.carbRatio?.toString() ?: "")) }
    var proteinRatio by remember { mutableStateOf(TextFieldValue(userInfo?.proteinRatio?.toString() ?: "")) }
    var fatRatio by remember { mutableStateOf(TextFieldValue(userInfo?.fatRatio?.toString() ?: "")) }
    var steps by remember { mutableStateOf(TextFieldValue(userInfo?.steps?.toString() ?: "")) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (userInfo != null) {
                item {
                    TextFieldWithLabel(
                        value = age,
                        onValueChange = { age = it },
                        label = "Edad",
                        isEditing = isEditing
                    )
                }
                item {
                    TextFieldWithLabel(
                        value = weight,
                        onValueChange = { weight = it },
                        label = "Peso",
                        isEditing = isEditing
                    )
                }
                item {
                    TextFieldWithLabel(
                        value = height,
                        onValueChange = { height = it },
                        label = "Altura",
                        isEditing = isEditing
                    )
                }
                item {
                    TextFieldWithLabel(
                        value = gender,
                        onValueChange = { gender = it },
                        label = "Género",
                        isEditing = isEditing
                    )
                }
                item {
                    TextFieldWithLabel(
                        value = activityLevel,
                        onValueChange = { activityLevel = it },
                        label = "Nivel de actividad",
                        isEditing = isEditing
                    )
                }
                item {
                    TextFieldWithLabel(
                        value = goalType,
                        onValueChange = { goalType = it },
                        label = "Tipo de objetivo",
                        isEditing = isEditing
                    )
                }
                item {
                    TextFieldWithLabel(
                        value = carbRatio,
                        onValueChange = { carbRatio = it },
                        label = "Carb Ratio",
                        isEditing = isEditing
                    )
                }
                item {
                    TextFieldWithLabel(
                        value = proteinRatio,
                        onValueChange = { proteinRatio = it },
                        label = "Protein Ratio",
                        isEditing = isEditing
                    )
                }
                item {
                    TextFieldWithLabel(
                        value = fatRatio,
                        onValueChange = { fatRatio = it },
                        label = "Fat Ratio",
                        isEditing = isEditing
                    )
                }
                item {
                    TextFieldWithLabel(
                        value = steps,
                        onValueChange = { steps = it },
                        label = "Steps",
                        isEditing = isEditing
                    )
                }
                item {
                    if (isEditing) {
                        Button(
                            onClick = {
                                val updatedUserInfo = UserInfo(
                                    gender = Gender.fromString(gender.text),
                                    age = age.text.toIntOrNull() ?: userInfo.age,
                                    height = height.text.toIntOrNull() ?: userInfo.height,
                                    weight = weight.text.toFloatOrNull() ?: userInfo.weight,
                                    activityLevel = ActivityLevel.fromString(activityLevel.text),
                                    goalType = GoalType.fromString(goalType.text),
                                    carbRatio = carbRatio.text.toFloatOrNull() ?: userInfo.carbRatio,
                                    proteinRatio = proteinRatio.text.toFloatOrNull() ?: userInfo.proteinRatio,
                                    fatRatio = fatRatio.text.toFloatOrNull() ?: userInfo.fatRatio,
                                    steps = steps.text.toIntOrNull() ?: userInfo.steps
                                )
                                viewModel.updateUserInfo(updatedUserInfo)
                                isEditing = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Guardar")
                        }
                    } else {
                        Button(
                            onClick = { isEditing = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Editar")
                        }
                    }
                }
            } else {
                item {
                    Text(text = "Cargando información del usuario...", style = MaterialTheme.typography.body1)
                }
            }
        }
    }
}

@Composable
fun TextFieldWithLabel(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    isEditing: Boolean
) {
    if (isEditing) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        Text(text = "$label: ${value.text}", style = MaterialTheme.typography.body1)
    }
}

fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT < 28) {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
