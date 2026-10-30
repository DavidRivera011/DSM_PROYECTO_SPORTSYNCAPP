
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



public interface CreateEventoMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      CreateEventoMutation.Data,
      CreateEventoMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val nombre: String,
  
    val fecha: com.google.firebase.dataconnect.LocalDate,
  
    val deporteId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val institucionId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val estadoId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val eventos_insert: EventosKey,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "CreateEvento"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun CreateEventoMutation.ref(
  
    nombre: String,fecha: com.google.firebase.dataconnect.LocalDate,deporteId: java.util.UUID,institucionId: java.util.UUID,estadoId: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    CreateEventoMutation.Data,
    CreateEventoMutation.Variables
  > =
  ref(
    
      CreateEventoMutation.Variables(
        nombre=nombre,fecha=fecha,deporteId=deporteId,institucionId=institucionId,estadoId=estadoId,
  
      )
    
  )

public suspend fun CreateEventoMutation.execute(

  
    
      nombre: String,fecha: com.google.firebase.dataconnect.LocalDate,deporteId: java.util.UUID,institucionId: java.util.UUID,estadoId: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    CreateEventoMutation.Data,
    CreateEventoMutation.Variables
  > =
  ref(
    
      nombre=nombre,fecha=fecha,deporteId=deporteId,institucionId=institucionId,estadoId=estadoId,
  
    
  ).execute()


