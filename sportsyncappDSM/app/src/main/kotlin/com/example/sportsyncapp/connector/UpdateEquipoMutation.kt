
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



public interface UpdateEquipoMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      UpdateEquipoMutation.Data,
      UpdateEquipoMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreEquipo: String,
  
    val institucionId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val deporteId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val equipos_update: EquiposKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "UpdateEquipo"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun UpdateEquipoMutation.ref(
  
    id: java.util.UUID,nombreEquipo: String,institucionId: java.util.UUID,deporteId: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    UpdateEquipoMutation.Data,
    UpdateEquipoMutation.Variables
  > =
  ref(
    
      UpdateEquipoMutation.Variables(
        id=id,nombreEquipo=nombreEquipo,institucionId=institucionId,deporteId=deporteId,
  
      )
    
  )

public suspend fun UpdateEquipoMutation.execute(

  
    
      id: java.util.UUID,nombreEquipo: String,institucionId: java.util.UUID,deporteId: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    UpdateEquipoMutation.Data,
    UpdateEquipoMutation.Variables
  > =
  ref(
    
      id=id,nombreEquipo=nombreEquipo,institucionId=institucionId,deporteId=deporteId,
  
    
  ).execute()


