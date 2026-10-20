
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



public interface UpdateMarcadorPartidoMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      UpdateMarcadorPartidoMutation.Data,
      UpdateMarcadorPartidoMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val marcadorLocal: Int,
  
    val marcadorVisitante: Int,
  
    val estadoId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val partidos_update: PartidosKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "UpdateMarcadorPartido"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun UpdateMarcadorPartidoMutation.ref(
  
    id: java.util.UUID,marcadorLocal: Int,marcadorVisitante: Int,estadoId: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    UpdateMarcadorPartidoMutation.Data,
    UpdateMarcadorPartidoMutation.Variables
  > =
  ref(
    
      UpdateMarcadorPartidoMutation.Variables(
        id=id,marcadorLocal=marcadorLocal,marcadorVisitante=marcadorVisitante,estadoId=estadoId,
  
      )
    
  )

public suspend fun UpdateMarcadorPartidoMutation.execute(

  
    
      id: java.util.UUID,marcadorLocal: Int,marcadorVisitante: Int,estadoId: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    UpdateMarcadorPartidoMutation.Data,
    UpdateMarcadorPartidoMutation.Variables
  > =
  ref(
    
      id=id,marcadorLocal=marcadorLocal,marcadorVisitante=marcadorVisitante,estadoId=estadoId,
  
    
  ).execute()


