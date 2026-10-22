
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



public interface UpdateInscripcionMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      UpdateInscripcionMutation.Data,
      UpdateInscripcionMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val estadoId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val inscripciones_update: InscripcionesKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "UpdateInscripcion"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun UpdateInscripcionMutation.ref(
  
    id: java.util.UUID,estadoId: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    UpdateInscripcionMutation.Data,
    UpdateInscripcionMutation.Variables
  > =
  ref(
    
      UpdateInscripcionMutation.Variables(
        id=id,estadoId=estadoId,
  
      )
    
  )

public suspend fun UpdateInscripcionMutation.execute(

  
    
      id: java.util.UUID,estadoId: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    UpdateInscripcionMutation.Data,
    UpdateInscripcionMutation.Variables
  > =
  ref(
    
      id=id,estadoId=estadoId,
  
    
  ).execute()


