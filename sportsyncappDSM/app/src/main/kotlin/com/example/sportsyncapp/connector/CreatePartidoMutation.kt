
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



public interface CreatePartidoMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      CreatePartidoMutation.Data,
      CreatePartidoMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val eventoId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val deporteId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val equipoLocalId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val equipoVisitanteId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val fecha: com.google.firebase.dataconnect.LocalDate,
  
    val hora: String,
  
    val estadoId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val partidos_insert: PartidosKey,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "CreatePartido"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun CreatePartidoMutation.ref(
  
    eventoId: java.util.UUID,deporteId: java.util.UUID,equipoLocalId: java.util.UUID,equipoVisitanteId: java.util.UUID,fecha: com.google.firebase.dataconnect.LocalDate,hora: String,estadoId: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    CreatePartidoMutation.Data,
    CreatePartidoMutation.Variables
  > =
  ref(
    
      CreatePartidoMutation.Variables(
        eventoId=eventoId,deporteId=deporteId,equipoLocalId=equipoLocalId,equipoVisitanteId=equipoVisitanteId,fecha=fecha,hora=hora,estadoId=estadoId,
  
      )
    
  )

public suspend fun CreatePartidoMutation.execute(

  
    
      eventoId: java.util.UUID,deporteId: java.util.UUID,equipoLocalId: java.util.UUID,equipoVisitanteId: java.util.UUID,fecha: com.google.firebase.dataconnect.LocalDate,hora: String,estadoId: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    CreatePartidoMutation.Data,
    CreatePartidoMutation.Variables
  > =
  ref(
    
      eventoId=eventoId,deporteId=deporteId,equipoLocalId=equipoLocalId,equipoVisitanteId=equipoVisitanteId,fecha=fecha,hora=hora,estadoId=estadoId,
  
    
  ).execute()


