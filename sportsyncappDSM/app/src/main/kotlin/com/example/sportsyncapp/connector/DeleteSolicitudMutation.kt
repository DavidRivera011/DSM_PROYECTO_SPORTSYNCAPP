
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



public interface DeleteSolicitudMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      DeleteSolicitudMutation.Data,
      DeleteSolicitudMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val solicitudes_delete: SolicitudesKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "DeleteSolicitud"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun DeleteSolicitudMutation.ref(
  
    id: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    DeleteSolicitudMutation.Data,
    DeleteSolicitudMutation.Variables
  > =
  ref(
    
      DeleteSolicitudMutation.Variables(
        id=id,
  
      )
    
  )

public suspend fun DeleteSolicitudMutation.execute(

  
    
      id: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    DeleteSolicitudMutation.Data,
    DeleteSolicitudMutation.Variables
  > =
  ref(
    
      id=id,
  
    
  ).execute()


