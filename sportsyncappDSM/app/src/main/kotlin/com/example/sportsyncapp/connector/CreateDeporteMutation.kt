
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



public interface CreateDeporteMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      CreateDeporteMutation.Data,
      CreateDeporteMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val nombreDeporte: String,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val deportes_insert: DeportesKey,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "CreateDeporte"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun CreateDeporteMutation.ref(
  
    nombreDeporte: String,

  
  
): com.google.firebase.dataconnect.MutationRef<
    CreateDeporteMutation.Data,
    CreateDeporteMutation.Variables
  > =
  ref(
    
      CreateDeporteMutation.Variables(
        nombreDeporte=nombreDeporte,
  
      )
    
  )

public suspend fun CreateDeporteMutation.execute(

  
    
      nombreDeporte: String,

  

  ): com.google.firebase.dataconnect.MutationResult<
    CreateDeporteMutation.Data,
    CreateDeporteMutation.Variables
  > =
  ref(
    
      nombreDeporte=nombreDeporte,
  
    
  ).execute()


