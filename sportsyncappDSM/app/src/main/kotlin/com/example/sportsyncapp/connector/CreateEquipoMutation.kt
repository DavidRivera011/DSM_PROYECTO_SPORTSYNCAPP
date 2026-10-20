
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



public interface CreateEquipoMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      CreateEquipoMutation.Data,
      CreateEquipoMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val nombreEquipo: String,
  
    val institucionId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val deporteId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val equipos_insert: EquiposKey,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "CreateEquipo"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun CreateEquipoMutation.ref(
  
    nombreEquipo: String,institucionId: java.util.UUID,deporteId: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    CreateEquipoMutation.Data,
    CreateEquipoMutation.Variables
  > =
  ref(
    
      CreateEquipoMutation.Variables(
        nombreEquipo=nombreEquipo,institucionId=institucionId,deporteId=deporteId,
  
      )
    
  )

public suspend fun CreateEquipoMutation.execute(

  
    
      nombreEquipo: String,institucionId: java.util.UUID,deporteId: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    CreateEquipoMutation.Data,
    CreateEquipoMutation.Variables
  > =
  ref(
    
      nombreEquipo=nombreEquipo,institucionId=institucionId,deporteId=deporteId,
  
    
  ).execute()


