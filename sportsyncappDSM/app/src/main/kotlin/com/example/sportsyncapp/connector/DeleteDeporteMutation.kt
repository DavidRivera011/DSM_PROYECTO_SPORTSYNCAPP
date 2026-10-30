
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



public interface DeleteDeporteMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      DeleteDeporteMutation.Data,
      DeleteDeporteMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val deportes_delete: DeportesKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "DeleteDeporte"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun DeleteDeporteMutation.ref(
  
    id: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    DeleteDeporteMutation.Data,
    DeleteDeporteMutation.Variables
  > =
  ref(
    
      DeleteDeporteMutation.Variables(
        id=id,
  
      )
    
  )

public suspend fun DeleteDeporteMutation.execute(

  
    
      id: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    DeleteDeporteMutation.Data,
    DeleteDeporteMutation.Variables
  > =
  ref(
    
      id=id,
  
    
  ).execute()


