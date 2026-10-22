
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



public interface DeleteInscripcionMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      DeleteInscripcionMutation.Data,
      DeleteInscripcionMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val inscripciones_delete: InscripcionesKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "DeleteInscripcion"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun DeleteInscripcionMutation.ref(
  
    id: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    DeleteInscripcionMutation.Data,
    DeleteInscripcionMutation.Variables
  > =
  ref(
    
      DeleteInscripcionMutation.Variables(
        id=id,
  
      )
    
  )

public suspend fun DeleteInscripcionMutation.execute(

  
    
      id: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    DeleteInscripcionMutation.Data,
    DeleteInscripcionMutation.Variables
  > =
  ref(
    
      id=id,
  
    
  ).execute()


