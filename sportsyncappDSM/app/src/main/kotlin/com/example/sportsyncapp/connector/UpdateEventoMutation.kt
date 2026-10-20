
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



public interface UpdateEventoMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      UpdateEventoMutation.Data,
      UpdateEventoMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombre: String,
  
    val fecha: com.google.firebase.dataconnect.LocalDate,
  
    val deporteId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val institucionId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val estadoId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val eventos_update: EventosKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "UpdateEvento"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun UpdateEventoMutation.ref(
  
    id: java.util.UUID,nombre: String,fecha: com.google.firebase.dataconnect.LocalDate,deporteId: java.util.UUID,institucionId: java.util.UUID,estadoId: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    UpdateEventoMutation.Data,
    UpdateEventoMutation.Variables
  > =
  ref(
    
      UpdateEventoMutation.Variables(
        id=id,nombre=nombre,fecha=fecha,deporteId=deporteId,institucionId=institucionId,estadoId=estadoId,
  
      )
    
  )

public suspend fun UpdateEventoMutation.execute(

  
    
      id: java.util.UUID,nombre: String,fecha: com.google.firebase.dataconnect.LocalDate,deporteId: java.util.UUID,institucionId: java.util.UUID,estadoId: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    UpdateEventoMutation.Data,
    UpdateEventoMutation.Variables
  > =
  ref(
    
      id=id,nombre=nombre,fecha=fecha,deporteId=deporteId,institucionId=institucionId,estadoId=estadoId,
  
    
  ).execute()


