
@file:Suppress(
  "KotlinRedundantDiagnosticSuppress",
  "PropertyName",
  "MayBeConstant",
  "RedundantVisibilityModifier",
  "RedundantCompanionReference",
  "RemoveEmptyClassBody",
  "SpellCheckingInspection",
  "unused",
)

package com.example.sportsyncapp.connector



public interface UpdateUsuarioMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      UpdateUsuarioMutation.Data,
      UpdateUsuarioMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val nombre: String,
  
    val apellido: String,
  
    val fechaNacimiento: com.google.firebase.dataconnect.LocalDate,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val usuarios_update: UsuariosKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "UpdateUsuario"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun UpdateUsuarioMutation.ref(
  
    nombre: String,apellido: String,fechaNacimiento: com.google.firebase.dataconnect.LocalDate,

  
  
): com.google.firebase.dataconnect.MutationRef<
    UpdateUsuarioMutation.Data,
    UpdateUsuarioMutation.Variables
  > =
  ref(
    
      UpdateUsuarioMutation.Variables(
        nombre=nombre,apellido=apellido,fechaNacimiento=fechaNacimiento,
  
      )
    
  )

public suspend fun UpdateUsuarioMutation.execute(

  
    
      nombre: String,apellido: String,fechaNacimiento: com.google.firebase.dataconnect.LocalDate,

  

  ): com.google.firebase.dataconnect.MutationResult<
    UpdateUsuarioMutation.Data,
    UpdateUsuarioMutation.Variables
  > =
  ref(
    
      nombre=nombre,apellido=apellido,fechaNacimiento=fechaNacimiento,
  
    
  ).execute()


