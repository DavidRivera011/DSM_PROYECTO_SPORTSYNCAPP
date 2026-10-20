
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



public interface UpdateDeporteMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      UpdateDeporteMutation.Data,
      UpdateDeporteMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreDeporte: String,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val deportes_update: DeportesKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "UpdateDeporte"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun UpdateDeporteMutation.ref(
  
    id: java.util.UUID,nombreDeporte: String,

  
  
): com.google.firebase.dataconnect.MutationRef<
    UpdateDeporteMutation.Data,
    UpdateDeporteMutation.Variables
  > =
  ref(
    
      UpdateDeporteMutation.Variables(
        id=id,nombreDeporte=nombreDeporte,
  
      )
    
  )

public suspend fun UpdateDeporteMutation.execute(

  
    
      id: java.util.UUID,nombreDeporte: String,

  

  ): com.google.firebase.dataconnect.MutationResult<
    UpdateDeporteMutation.Data,
    UpdateDeporteMutation.Variables
  > =
  ref(
    
      id=id,nombreDeporte=nombreDeporte,
  
    
  ).execute()


