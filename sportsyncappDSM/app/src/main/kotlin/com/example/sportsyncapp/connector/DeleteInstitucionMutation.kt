
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



public interface DeleteInstitucionMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      DeleteInstitucionMutation.Data,
      DeleteInstitucionMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val instituciones_delete: InstitucionesKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "DeleteInstitucion"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun DeleteInstitucionMutation.ref(
  
    id: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    DeleteInstitucionMutation.Data,
    DeleteInstitucionMutation.Variables
  > =
  ref(
    
      DeleteInstitucionMutation.Variables(
        id=id,
  
      )
    
  )

public suspend fun DeleteInstitucionMutation.execute(

  
    
      id: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    DeleteInstitucionMutation.Data,
    DeleteInstitucionMutation.Variables
  > =
  ref(
    
      id=id,
  
    
  ).execute()


